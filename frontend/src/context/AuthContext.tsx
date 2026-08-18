"use client";
import {
  createContext,
  useState,
  useEffect,
  useContext,
  type ReactNode,
  useRef,
  useCallback,
} from "react";
import axios, { AxiosError, type InternalAxiosRequestConfig } from "axios";
import { useNavigate, useLocation } from "react-router-dom";
import { jwtDecode, type JwtPayload } from "jwt-decode";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "http://localhost:8000/api/v1";

interface AuthProviderProps {
  children: ReactNode;
}

interface UserData {
  username: string;
  email: string;
  password: string;
  rememberUser: boolean;
}

interface UserInformation {
  username: string;
  email: string;
  id: string;
  photoLink: string;
  faculty: string;
  bio: string;
  activeIn:  Set<String> ;
  dateJoined: Date;
  posts: number;
  comments: number;
}

interface TokenPayload extends JwtPayload {
  sub: string;
  role: string;
}

interface AuthResponse {
  isSuccess: boolean;
  message: string;
  status: number;
  role?: string;
}

interface AuthContextType {
  user: UserInformation | null;
  tokenData: TokenPayload | null;
  postAuthRedirect: string | null;
  isRestoring: boolean;
  getAccessToken: () => string | null;
  login: (username: string, password: string, remembeMe?: boolean, redirectOnSuccess?: boolean) => Promise<AuthResponse>;
  register: (userData: UserData, redirectOnSuccess?: boolean) => Promise<AuthResponse>;
  logout: () => Promise<AuthResponse>;
  clearSession: () => void;
}

export const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true,
});

const AuthContext = createContext<AuthContextType | null>(null);

let refreshPromise: Promise<string> | null = null;

const refreshAccessToken = (): Promise<string> => {
  if (refreshPromise) return refreshPromise;

  refreshPromise = axios
    .post(`${API_BASE_URL}/auth/refresh`, {}, { withCredentials: true })
    .then((res) => res.data.accessToken)
    .finally(() => {
      refreshPromise = null;
    });

  return refreshPromise;
};

export const AuthProvider = ({ children }: AuthProviderProps) => {
  const [user, setUser] = useState<UserInformation | null>(null);
  const tokenRawRef = useRef<string | null>(null);
  const [tokenData, setTokenData] = useState<TokenPayload | null>(null);
  const [postAuthRedirect, setPostAuthRedirect] = useState<string | null>(null);
  const [isRestoring, setIsRestoring] = useState(true);
  const router = useNavigate();
  const location = useLocation();

  const attachAccessToken = useCallback((config: InternalAxiosRequestConfig) => {
    if (typeof window !== "undefined") {
      if (tokenRawRef.current) {
        config.headers.set("Authorization", `Bearer ${tokenRawRef.current}`);
      }
    }
    return config;
  }, []);

  //Interceptor mount 
  useEffect(() => {
    const responseInterceptor = api.interceptors.response.use(
      (response) => response,
      async (error) => {
        const originalRequest = error.config;

        if (error.response?.status === 401 && !originalRequest._retry) {
          originalRequest._retry = true;
          console.log("Access token expired. Attempting background refresh...");

          try {
            //To prevent endless 401 loop DO NOT use api here just normal axios or fetch
            const response = await axios.post(`${API_BASE_URL}/auth/refresh`,
              {},
              {
                withCredentials: true
              }
            );

            const newAccessToken = response.data.accessToken;
            console.log("Background token refresh successful!");

            tokenRawRef.current = newAccessToken;
            const decodedToken = jwtDecode<TokenPayload>(newAccessToken);
            setTokenData(decodedToken);

            originalRequest.headers["Authorization"] =
              `Bearer ${newAccessToken}`;
            return api(originalRequest);
          } catch (refreshError) {
            console.error(
              "Refresh token expired or invalid. Evicting session.",
              refreshError,
            );
            tokenRawRef.current = null;
            setTokenData(null);
            setUser(null);
            return Promise.reject(refreshError);
          }
        }

        return Promise.reject(error);
      },
    );

    return () => {
      api.interceptors.response.eject(responseInterceptor);
    };
  }, []);

  //Acquire access token on every page refresh
  useEffect(() => {
    async function verifyUser() {
      if (tokenRawRef.current) {
        setIsRestoring(false);
        return;
      }
      try {
        const newAccessToken = await refreshAccessToken();
        console.log("Session successfully restored");
        tokenRawRef.current = newAccessToken;
        const decodedToken = jwtDecode<TokenPayload>(newAccessToken);
        setTokenData(decodedToken);
        const redirect = sessionStorage.getItem("redirectAfterSessionRestored");
        sessionStorage.removeItem("redirectAfterSessionRestored");
        const landingPath = window.location.pathname;
        if (
          redirect &&
          redirect !== landingPath &&
          (landingPath === "/" || landingPath === "/login")
        ) {
          router(redirect);
        }
      } catch (error) {
        console.log("Session not restored please login: ", error);
        if (error instanceof AxiosError && error.response) {
          console.log(error.response);
        }
      } finally {
        setIsRestoring(false);
      }
    }
    verifyUser();

    const id = api.interceptors.request.use(attachAccessToken, (error) => Promise.reject(error));
    return () => api.interceptors.request.eject(id);
  // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const activePostAuthRedirect =
    postAuthRedirect &&
    (location.pathname === "/" || location.pathname === "/login")
      ? postAuthRedirect
      : null;

  const login = async (identifier: string, password: string, remembeMe: boolean = false, redirectOnSuccess: boolean = false): Promise<AuthResponse> => {
    try {
      const params = new URLSearchParams();
      params.append("identifier", identifier);
      params.append("password", password);
      params.append("rememberUser", remembeMe ? "true" : "false");

      const response = await axios.post(
        `${API_BASE_URL}/auth/login`,
        params,
        {
          headers: { "Content-Type": "application/x-www-form-urlencoded" },
          withCredentials: true
        },
      );

      const newAccessToken = response.data.accessToken;
      tokenRawRef.current = newAccessToken;

      const decodedToken = jwtDecode<TokenPayload>(newAccessToken);
      setTokenData(decodedToken);
      setUser(response.data.userData);

      console.log("Login successful");

      const pendingRedirect = sessionStorage.getItem("redirectAfterSessionRestored");
      sessionStorage.removeItem("redirectAfterSessionRestored");

      if (redirectOnSuccess && pendingRedirect) {
        router(pendingRedirect);
      }
      return {
          isSuccess: true,
          message: "Login successful!",
          status: response.status,
          role: decodedToken.role,
      };
    } catch (error: unknown) {
      console.error("Login failed:", error);
      if (error instanceof AxiosError) {
        if (error.response) {
          console.error("Response status:", error.response.status);
          console.error("Response data:", error.response.data);
          return {
            isSuccess: false,
            message: error.response.data?.detail ?? "Invalid credentials.",
            status: error.response.status,
          };
        }
        if (error.request || error.code === "ERR_NETWORK" || error.code === "ECONNREFUSED") {
          console.error("No response received:", error.request);
          return {
            isSuccess: false,
            message: "SERVICE_UNAVAILABLE",
            status: 503,
          };
        }
      }
      return {
        isSuccess: false,
        message: "LOGIN_UNEXPECTED_ERROR",
        status: 500,
      };
    }
  };

  const register = async (userData: UserData, redirectOnSuccess: boolean = false): Promise<AuthResponse> => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/auth/register`,
        userData,
        {
          headers: { "Content-Type": "application/json" },
          withCredentials: true
        },
      );

      const newAccessToken = response.data.accessToken;
      tokenRawRef.current = newAccessToken;

      const decodedToken = jwtDecode<TokenPayload>(newAccessToken)

      const pendingRedirect = sessionStorage.getItem("redirectAfterSessionRestored");
      sessionStorage.removeItem("redirectAfterSessionRestored");

      const destination =
        redirectOnSuccess && pendingRedirect
          ? pendingRedirect
          : "/";

      setPostAuthRedirect(destination);
      setTokenData(decodedToken);
      setUser(response.data.userData);

      console.log("Account registration successful");

      router(destination);
      return {
        isSuccess: true,
        message: "Registration successful!",
        status: response.status,
        role: decodedToken.role,
      };
    } catch (error: unknown) {
      console.error("Signup failed:", error);
      if (error instanceof AxiosError) {
        if (error.response) {
          return {
            isSuccess: false,
            message: error.response.data?.detail ?? "Invalid credentials.",
            status: error.response.status,
          };
        }
        if (error.request || error.code === "ERR_NETWORK" || error.code === "ECONNREFUSED") {
          console.error("No response received:", error.request);
          return {
            isSuccess: false,
            message: "SERVICE_UNAVAILABLE",
            status: 503,
          };
        }
      }
      return {
        isSuccess: false,
        message: "REGISTER_UNEXPECTED_ERROR",
        status: 500,
      };
    }
  };

  const clearSession = useCallback(() => {
    tokenRawRef.current = null;
    setUser(null);
    setTokenData(null);
    setPostAuthRedirect(null);
  }, []);

  const logout = async (): Promise<AuthResponse> => {
    try {
      const response = await axios.post(
        `${API_BASE_URL}/auth/logout`,
        {},
        {
          headers: { Authorization: `Bearer ${tokenRawRef.current}` },
          withCredentials: true,
        },
      );
      return {
        isSuccess: true,
        message: "Logout successful",
        status: response.status
      };
    } catch (error) {
      console.error("Logout failed:", error);
      if (error instanceof AxiosError && error.response) { 
        return {
          isSuccess: false,
          message: error.response?.data?.detail,
          status: error.response.status
        };
      }
      return {
          isSuccess: false,
          message: "The server could not complete logout",
          status: 503,
      };
    } finally {
      clearSession();
      sessionStorage.removeItem("redirectAfterSessionRestored");
    }
  };

  const getAccessToken = useCallback(() => tokenRawRef.current, []);

  return (
    <AuthContext.Provider value={{ user, tokenData, postAuthRedirect: activePostAuthRedirect, getAccessToken, login, register, logout, clearSession, isRestoring }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export default AuthContext;
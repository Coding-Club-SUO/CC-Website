import { useForm, type SubmitHandler } from 'react-hook-form';
import { login } from '@/api/auth/auth';
import './AuthForm.css';

interface LoginInputs {
  identifier: string;
  password: string;
  rememberMe: boolean;
}

interface LoginFormProps {
  onSwitchToRegister?: () => void;
}

export const LoginForm = ({ onSwitchToRegister }: LoginFormProps) => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginInputs>({
    defaultValues: {
      rememberMe: false,
    },
  });

  const onSubmit: SubmitHandler<LoginInputs> = async (data) => {
    try {
      await login(data.identifier, data.password, data.rememberMe);
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <div className="auth-card">
      <h2>Sign In</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label htmlFor="login-identifier">Username or email</label>
          <input
            id="login-identifier"
            type="text"
            {...register('identifier', {
              required: 'Username or email required',
            })}
          />
          {errors.identifier && (
            <span className="error-message">{errors.identifier.message}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="login-password">Password</label>
          <input
            id="login-password"
            type="password"
            {...register('password', {
              required: 'Password is required',
            })}
          />
          {errors.password && (
            <span className="error-message">{errors.password.message}</span>
          )}
        </div>

        <div className="form-group checkbox-group">
          <label htmlFor="login-rememberMe" className="checkbox-label">
            <input
              id="login-rememberMe"
              type="checkbox"
              {...register('rememberMe')}
            />
            Remember me
          </label>
        </div>

        <button type="submit" className="submit-btn" disabled={isSubmitting}>
          {isSubmitting ? 'Logging in...' : 'Log In'}
        </button>
      </form>

      {onSwitchToRegister && (
        <button type="button" className="toggle-btn" onClick={onSwitchToRegister}>
          Don't have an account? Register
        </button>
      )}
    </div>
  );
};
import { useForm, type SubmitHandler } from 'react-hook-form';
import { register as authRegister } from '@/api/auth/auth';
import './AuthForm.css';

interface RegisterInputs {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  rememberMe: boolean;
}

interface RegisterFormProps {
  onSwitchToLogin?: () => void;
}

export const RegisterForm = ({ onSwitchToLogin }: RegisterFormProps) => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<RegisterInputs>({
    defaultValues: {
      rememberMe: false,
    },
  });

  const password = watch('password');

  const onSubmit: SubmitHandler<RegisterInputs> = async (data) => {
    try {
      await authRegister({
        email: data.email,
        password: data.password,
        username: data.username,
        rememberUser: data.rememberMe,
      });
    } catch (error) {
      console.error('Registration failed:', error);
    }
  };

  return (
    <div className="auth-card">
      <h2>Create Account</h2>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div className="form-group">
          <label htmlFor="reg-name">Username</label>
          <input
            id="reg-name"
            type="text"
            {...register('username', { required: 'username is required' })}
          />
          {errors.username && (
            <span className="error-message">{errors.username.message}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="reg-email">Email</label>
          <input
            id="reg-email"
            type="email"
            {...register('email', {
              required: 'Email is required',
              pattern: {
                value: /^\S+@\S+$/i,
                message: 'Invalid email address',
              },
            })}
          />
          {errors.email && (
            <span className="error-message">{errors.email.message}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="reg-password">Password</label>
          <input
            id="reg-password"
            type="password"
            {...register('password', {
              required: 'Password is required',
              minLength: {
                value: 6,
                message: 'Password must be at least 6 characters',
              },
            })}
          />
          {errors.password && (
            <span className="error-message">{errors.password.message}</span>
          )}
        </div>

        <div className="form-group">
          <label htmlFor="confirmPassword">Confirm Password</label>
          <input
            id="confirmPassword"
            type="password"
            {...register('confirmPassword', {
              required: 'Please confirm your password',
              validate: (val) => val === password || 'Passwords do not match',
            })}
          />
          {errors.confirmPassword && (
            <span className="error-message">{errors.confirmPassword.message}</span>
          )}
        </div>

        <div className="form-group checkbox-group">
          <label htmlFor="rememberMe" className="checkbox-label">
            <input
              id="rememberMe"
              type="checkbox"
              {...register('rememberMe')}
            />
            Remember me
          </label>
        </div>

        <button type="submit" className="submit-btn" disabled={isSubmitting}>
          {isSubmitting ? 'Registering...' : 'Register'}
        </button>
      </form>

      {onSwitchToLogin && (
        <button type="button" className="toggle-btn" onClick={onSwitchToLogin}>
          Already have an account? Log In
        </button>
      )}
    </div>
  );
};
import { useState } from 'react';
import { LoginForm } from '../components/login/LoginForm';
import { RegisterForm } from '../components/login/RegisterForm';

type AuthView = 'login' | 'register';

export const AuthPage = () => {
  const [view, setView] = useState<AuthView>('login');

  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        minHeight: '100vh',
        backgroundColor: '#f4f6f8',
      }}
    >
      {view === 'login' ? (
        <LoginForm onSwitchToRegister={() => setView('register')} />
      ) : (
        <RegisterForm onSwitchToLogin={() => setView('login')} />
      )}
    </div>
  );
};
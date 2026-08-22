'use client';

import { AuthPage } from '../../views/authpage/AuthPage'
import { AuthProvider } from '../../components/context/AuthContext'

export default function Login() {
  return (
    <AuthProvider>
      <AuthPage />
    </AuthProvider>
  )
}
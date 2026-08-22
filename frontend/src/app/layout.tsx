import type { Metadata } from 'next'
import Navbar from '../components/navbar/Navbar'
import Providers from './providers'
import './globals.css'

export const metadata: Metadata = {
  title: 'Coding Club Resource Hub',
  description: 'Course resources and community for UBC Okanagan students.',
  icons: {
    icon: '/codingclub_ok_logo.jpeg',
  },
}

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en">
      <body>
        <Navbar />
        <Providers>{children}</Providers>
      </body>
    </html>
  )
}
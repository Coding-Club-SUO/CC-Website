import type { Metadata } from 'next'
import Navbar from '../components/navbar/Navbar'
import Footer from '../components/footer/Footer'
import Providers from './providers'
import './globals.css'

export const metadata: Metadata = {
  title: 'CSCU | UBC Okanagan Computer Science Course Union',
  description: 'The Computer Science Course Union at UBC Okanagan: a department-backed hub for Computer Science and Data Science students, run by students.',
  icons: {
    icon: '/cscu_ok_logo.png',
  },
}

// Runs before first paint so the saved / system theme never flashes.
const THEME_SCRIPT = `try{var t=localStorage.getItem('theme');if(t!=='light'&&t!=='dark'){t=matchMedia('(prefers-color-scheme: light)').matches?'light':'dark'}document.documentElement.dataset.theme=t}catch(e){}`

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <head>
        <script dangerouslySetInnerHTML={{ __html: THEME_SCRIPT }} />
      </head>
      <body>
        <Navbar />
        <Providers>
          <main>{children}</main>
        </Providers>
        <Footer />
      </body>
    </html>
  )
}
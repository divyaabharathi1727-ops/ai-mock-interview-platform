import Link from "next/link";
import { AuthForm } from "@/components/AuthForm";
import { ThemeToggle } from "@/components/ThemeToggle";

export default function LoginPage() {
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/"><span className="brand-mark">●</span> Interview Lab</Link><div className="nav-actions"><ThemeToggle /><Link className="text-link" href="/register">Create account</Link></div></nav><section className="auth-layout"><div className="intro"><span className="eyebrow">Welcome back</span><h1>Pick up where your preparation left off.</h1><p className="intro-copy">Your practice history and next best question are waiting.</p></div><div className="auth-card"><h2>Sign in</h2><p>Use the email and password tied to your account.</p><AuthForm mode="login" /></div></section></main>;
}

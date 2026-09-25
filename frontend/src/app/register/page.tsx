import Link from "next/link";
import { AuthForm } from "@/components/AuthForm";
import { ThemeToggle } from "@/components/ThemeToggle";

export default function RegisterPage() {
  return <main className="page-shell"><nav className="topbar"><Link className="brand" href="/"><span className="brand-mark">●</span> Interview Lab</Link><div className="nav-actions"><ThemeToggle /><Link className="text-link" href="/login">Sign in</Link></div></nav><section className="auth-layout"><div className="intro"><span className="eyebrow">Begin here</span><h1>Turn uncertainty into a repeatable practice.</h1><p className="intro-copy">Build confidence one thoughtful answer at a time.</p></div><div className="auth-card"><h2>Create your account</h2><p>It takes less than a minute to get started.</p><AuthForm mode="register" /></div></section></main>;
}

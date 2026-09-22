import Link from "next/link";

export default function HomePage() {
  return (
    <main className="page-shell">
      <nav className="topbar"><Link className="brand" href="/"><span className="brand-mark">●</span> Interview Lab</Link><Link className="text-link" href="/login">Sign in</Link></nav>
      <section className="auth-layout">
        <div className="intro"><span className="eyebrow">A better rehearsal room</span><h1>Make your next answer your strongest one.</h1><p className="intro-copy">Practice technical and behavioral interviews with a clear plan, useful feedback, and a little less pressure.</p></div>
        <div className="auth-card"><h2>Start with a focused session.</h2><p>Create your free account and keep your preparation in one place.</p><Link className="primary-button" href="/register" style={{ display: "block", textAlign: "center", textDecoration: "none" }}>Create account</Link></div>
      </section>
    </main>
  );
}

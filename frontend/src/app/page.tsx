import Link from "next/link";
import { ThemeToggle } from "@/components/ThemeToggle";

export default function HomePage() {
  return (
    <main className="page-shell">
      <nav className="topbar"><Link className="brand" href="/"><span className="brand-mark">●</span> Interview Lab</Link><div className="nav-actions"><ThemeToggle /><Link className="text-link" href="/login">Sign in</Link></div></nav>
      <section className="auth-layout">
        <div className="intro"><span className="eyebrow">Interview Lab / practice with purpose</span><h1>Practice smarter. Interview with confidence.</h1><p className="intro-copy">A calmer way to rehearse technical and behavioral interviews, get useful feedback, and see exactly where to improve.</p><div className="hero-points"><span>20-question sessions</span><span>Mixed question formats</span><span>Private progress tracking</span></div></div>
        <div className="auth-card"><span className="eyebrow">Your next session</span><h2>Build confidence one answer at a time.</h2><p>Create your workspace and turn preparation into a habit.</p><Link className="primary-button" href="/register" style={{ display: "block", textAlign: "center", textDecoration: "none" }}>Start mock interview</Link></div>
      </section>
      <section className="landing-features" aria-label="Platform features">
        <div><span className="eyebrow">01 / Practice</span><h2>Questions that feel like the real thing.</h2><p>Role-aware sessions combine open responses with quick objective prompts, so preparation stays varied and focused.</p></div>
        <div><span className="eyebrow">02 / Feedback</span><h2>Know what to work on next.</h2><p>Review objective accuracy, AI feedback, timing, and performance trends from your own sessions.</p></div>
        <div><span className="eyebrow">03 / Progress</span><h2>A calmer path to confidence.</h2><p>Keep your history in one place and return to practice with a clear, personal next step.</p></div>
      </section>
    </main>
  );
}

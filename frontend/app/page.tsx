import Link from "next/link";
import { ArrowRight, BarChart3, BrainCircuit, CheckCircle2, PlayCircle, ShieldCheck, Sparkles, Star, Target } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Navbar } from "@/components/navbar";

const features = [
  { icon: BrainCircuit, title: "AI-powered feedback", description: "Get instant interview critique across technical fluency, clarity, and relevance." },
  { icon: BarChart3, title: "Performance analytics", description: "Track trends, confidence, and improvement scores across every mock interview." },
  { icon: Target, title: "Role-based practice", description: "Simulate interviews for frontend, backend, data, and other target roles." },
];

const steps = [
  { title: "Pick a role and format", description: "Choose your job track, interview type, and difficulty." },
  { title: "Answer realistic questions", description: "Practice under timed conditions with focused behavioral and technical prompts." },
  { title: "Review AI insights", description: "Measure your strengths, weak points, and improvements to work on next." },
];

export default function HomePage() {
  return (
    <div className="min-h-screen bg-slate-50 text-slate-900">
      <Navbar />

      <main>
        <section className="relative overflow-hidden">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_top_left,_rgba(15,23,42,0.08),_transparent_30%),radial-gradient(circle_at_bottom_right,_rgba(148,163,184,0.12),_transparent_35%)]" />
          <div className="container-shell relative py-16 sm:py-20 lg:py-24">
            <div className="grid items-center gap-10 lg:grid-cols-[1.2fr_0.8fr]">
              <div>
                <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-slate-200 bg-white/80 px-3 py-1.5 text-sm font-medium text-slate-700 shadow-sm backdrop-blur">
                  <Sparkles className="h-4 w-4 text-violet-500" />
                  Trusted by students and early-career professionals
                </div>
                <h1 className="max-w-xl text-4xl font-black tracking-tight text-slate-900 sm:text-5xl lg:text-6xl">
                  Practice interviews. Get AI feedback. Improve with every attempt.
                </h1>
                <p className="mt-6 max-w-xl text-lg leading-8 text-slate-600">
                  Build interview confidence with realistic mock conversations, targeted coaching, and clear performance analytics tailored to your next role.
                </p>
                <div className="mt-8 flex flex-col gap-3 sm:flex-row">
                  <Link href="/login">
                    <Button className="rounded-full bg-slate-900 px-6 py-3 text-white hover:bg-slate-800">
                      <PlayCircle className="mr-2 h-4 w-4" />
                      Start Interview
                    </Button>
                  </Link>
                  <a href="#features">
                    <Button variant="outline" className="rounded-full border-slate-200 bg-white px-6 py-3 text-slate-700 hover:bg-slate-100">
                      Explore Features
                    </Button>
                  </a>
                </div>

                <div className="mt-10 flex flex-wrap items-center gap-6 text-sm text-slate-600">
                  <div className="flex items-center gap-2">
                    <CheckCircle2 className="h-4 w-4 text-emerald-500" />
                    3,200+ mock interviews
                  </div>
                  <div className="flex items-center gap-2">
                    <Star className="h-4 w-4 text-amber-500" />
                    4.9/5 student rating
                  </div>
                </div>
              </div>

              <div className="relative">
                <div className="rounded-[2rem] border border-slate-200 bg-white p-4 shadow-[0_30px_80px_rgba(15,23,42,0.08)]">
                  <div className="rounded-[1.5rem] bg-slate-900 p-6 text-white">
                    <div className="mb-6 flex items-center justify-between">
                      <div>
                        <p className="text-xs uppercase tracking-[0.2em] text-slate-300">Interview prep</p>
                        <h2 className="mt-2 text-2xl font-bold">Frontend Developer</h2>
                      </div>
                      <div className="rounded-full bg-white/10 px-2.5 py-1 text-xs font-medium text-slate-200">Technical</div>
                    </div>

                    <div className="rounded-2xl bg-white/5 p-4">
                      <p className="text-sm text-slate-300">Question 3 of 5</p>
                      <p className="mt-3 text-lg font-medium">How would you optimize a React app for performance under heavy list rendering?</p>
                    </div>

                    <div className="mt-6 grid gap-4 sm:grid-cols-3">
                      <div className="rounded-xl bg-white/5 p-3">
                        <p className="text-xs text-slate-300">Accuracy</p>
                        <p className="mt-2 text-xl font-bold">84%</p>
                      </div>
                      <div className="rounded-xl bg-white/5 p-3">
                        <p className="text-xs text-slate-300">Clarity</p>
                        <p className="mt-2 text-xl font-bold">91%</p>
                      </div>
                      <div className="rounded-xl bg-white/5 p-3">
                        <p className="text-xs text-slate-300">Confidence</p>
                        <p className="mt-2 text-xl font-bold">76%</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section id="features" className="py-20">
          <div className="container-shell">
            <div className="max-w-2xl">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-500">Why students use it</p>
              <h2 className="section-title mt-3">Everything you need to interview with confidence.</h2>
            </div>
            <div className="mt-10 grid gap-6 md:grid-cols-3">
              {features.map(({ icon: Icon, title, description }) => (
                <div key={title} className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm">
                  <div className="flex h-12 w-12 items-center justify-center rounded-2xl bg-slate-900 text-white">
                    <Icon className="h-5 w-5" />
                  </div>
                  <h3 className="mt-5 text-xl font-semibold text-slate-900">{title}</h3>
                  <p className="mt-3 text-sm leading-7 text-slate-600">{description}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section id="how-it-works" className="bg-white py-20">
          <div className="container-shell">
            <div className="text-center">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-500">How it works</p>
              <h2 className="section-title mt-3">Simple prep. Clear feedback. Better outcomes.</h2>
            </div>
            <div className="mt-12 grid gap-6 md:grid-cols-3">
              {steps.map((step, index) => (
                <div key={step.title} className="rounded-3xl border border-slate-200 bg-slate-50 p-6 text-left">
                  <div className="mb-5 flex h-10 w-10 items-center justify-center rounded-full bg-slate-900 text-sm font-bold text-white">
                    0{index + 1}
                  </div>
                  <h3 className="text-xl font-semibold text-slate-900">{step.title}</h3>
                  <p className="mt-3 text-sm leading-7 text-slate-600">{step.description}</p>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section className="py-20">
          <div className="container-shell grid gap-8 lg:grid-cols-[0.9fr_1.1fr] lg:items-center">
            <div className="rounded-[2rem] border border-slate-200 bg-white p-6 shadow-sm">
              <div className="mb-4 flex items-center gap-2 text-emerald-600">
                <ShieldCheck className="h-5 w-5" />
                <span className="text-sm font-semibold uppercase tracking-[0.18em]">AI evaluation</span>
              </div>
              <h2 className="text-3xl font-bold tracking-tight text-slate-900">Actionable coaching, not generic feedback.</h2>
              <p className="mt-4 text-base leading-7 text-slate-600">
                The platform evaluates your answers for technical accuracy, communication quality, relevance, and completeness so you know exactly where to improve.
              </p>
            </div>
            <div className="grid gap-4 sm:grid-cols-2">
              {[
                { label: "Technical accuracy", value: "89%" },
                { label: "Communication", value: "91%" },
                { label: "Relevance", value: "86%" },
                { label: "Completeness", value: "82%" },
              ].map((item) => (
                <div key={item.label} className="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
                  <p className="text-sm text-slate-500">{item.label}</p>
                  <div className="mt-4 flex items-center justify-between">
                    <span className="text-3xl font-bold text-slate-900">{item.value}</span>
                    <div className="h-2.5 w-20 overflow-hidden rounded-full bg-slate-200">
                      <div className="h-full rounded-full bg-slate-900" style={{ width: item.value }} />
                    </div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>

        <section id="analytics" className="bg-white py-20">
          <div className="container-shell">
            <div className="grid gap-8 lg:grid-cols-[1fr_0.9fr] lg:items-center">
              <div>
                <p className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-500">Performance analytics</p>
                <h2 className="section-title mt-3">Track your progress from mock interview to final round.</h2>
                <p className="section-subtitle">
                  Review your score trends, spot recurring weak points, and focus your preparation where it matters most.
                </p>
              </div>
              <div className="rounded-[2rem] border border-slate-200 bg-slate-50 p-6 shadow-sm">
                <div className="mb-6 flex items-center justify-between">
                  <div>
                    <p className="text-sm text-slate-500">Overall improvement</p>
                    <p className="mt-2 text-4xl font-bold text-slate-900">+29%</p>
                  </div>
                  <div className="rounded-full bg-emerald-100 px-2.5 py-1 text-sm font-semibold text-emerald-700">Strong growth</div>
                </div>
                <div className="space-y-4">
                  {[
                    { label: "Problem framing", value: 82 },
                    { label: "System design", value: 76 },
                    { label: "Communication", value: 88 },
                  ].map((stat) => (
                    <div key={stat.label}>
                      <div className="mb-2 flex items-center justify-between text-sm text-slate-600">
                        <span>{stat.label}</span>
                        <span>{stat.value}%</span>
                      </div>
                      <div className="h-2.5 overflow-hidden rounded-full bg-slate-200">
                        <div className="h-full rounded-full bg-slate-900" style={{ width: `${stat.value}%` }} />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </section>

        <section className="py-20">
          <div className="container-shell">
            <div className="rounded-[2rem] border border-slate-200 bg-slate-900 px-6 py-10 text-center text-white shadow-[0_30px_80px_rgba(15,23,42,0.15)] sm:px-10 lg:px-16">
              <p className="text-sm font-semibold uppercase tracking-[0.18em] text-slate-300">Ready to begin?</p>
              <h2 className="mt-4 text-3xl font-bold tracking-tight sm:text-4xl">Start practicing for your next interview today.</h2>
              <p className="mx-auto mt-4 max-w-xl text-slate-300">
                Build your confidence with realistic questions, smart AI feedback, and a roadmap for improvement.
              </p>
              <div className="mt-8 flex flex-col justify-center gap-3 sm:flex-row">
                <Link href="/login">
                  <Button className="rounded-full bg-white px-6 py-3 text-slate-900 hover:bg-slate-100">
                    Start free
                    <ArrowRight className="ml-2 h-4 w-4" />
                  </Button>
                </Link>
                <Link href="/dashboard">
                  <Button variant="outline" className="rounded-full border-white/20 bg-white/5 px-6 py-3 text-white hover:bg-white/10">
                    View dashboard
                  </Button>
                </Link>
              </div>
            </div>
          </div>
        </section>
      </main>

      <footer className="border-t border-slate-200 bg-white">
        <div className="container-shell flex flex-col gap-3 py-8 text-sm text-slate-600 sm:flex-row sm:items-center sm:justify-between">
          <p>© 2026 MockInterview. Built for student success.</p>
          <div className="flex items-center gap-6">
            <Link href="/login" className="hover:text-slate-900">Login</Link>
            <Link href="/register" className="hover:text-slate-900">Register</Link>
            <Link href="/dashboard" className="hover:text-slate-900">Dashboard</Link>
          </div>
        </div>
      </footer>
    </div>
  );
}

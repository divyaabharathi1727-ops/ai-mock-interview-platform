"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { ArrowLeft, CheckCircle2, LoaderCircle } from "lucide-react";
import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { QuestionCard } from "@/components/question-card";

const jobRoles = [
  "Frontend Developer",
  "Backend Developer",
  "Full Stack Developer",
  "Java Developer",
  "Python Developer",
  "Data Analyst",
  "Custom Role",
];

const interviewTypes = ["Technical", "Behavioral", "Mixed"];
const difficulties = ["Easy", "Medium", "Hard"];

const questions = [
  "Describe a project where you improved performance and explain the trade-offs you considered.",
  "How would you handle a production issue that impacts multiple services and users are reporting errors?",
  "Tell me about a time you collaborated with a teammate to resolve a complex technical challenge.",
  "What metrics would you use to evaluate whether a new feature is successful in production?",
  "How do you balance shipping quickly against writing maintainable and scalable code?",
];

export default function InterviewPage() {
  const router = useRouter();
  const [selectedRole, setSelectedRole] = useState("Frontend Developer");
  const [selectedType, setSelectedType] = useState("Technical");
  const [selectedDifficulty, setSelectedDifficulty] = useState("Medium");
  const [isSetupComplete, setIsSetupComplete] = useState(false);
  const [currentQuestion, setCurrentQuestion] = useState(0);
  const [answers, setAnswers] = useState<string[]>(Array(questions.length).fill(""));
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState("");

  const activeAnswer = answers[currentQuestion] ?? "";
  const completion = useMemo(
    () => ((currentQuestion + 1) / questions.length) * 100,
    [currentQuestion],
  );

  const handleAnswerChange = (value: string) => {
    const next = [...answers];
    next[currentQuestion] = value;
    setAnswers(next);
  };

  const handleSubmitAnswer = async () => {
    if (!activeAnswer.trim()) {
      setError("Please provide an answer before continuing.");
      return;
    }

    setError("");
    setIsSubmitting(true);
    await new Promise((resolve) => setTimeout(resolve, 700));
    setIsSubmitting(false);

    if (currentQuestion < questions.length - 1) {
      setCurrentQuestion((value) => value + 1);
      return;
    }

    router.push("/results");
  };

  const startInterview = () => {
    setIsSetupComplete(true);
  };

  if (!isSetupComplete) {
    return (
      <div className="min-h-screen bg-slate-50 px-4 py-10 sm:px-6 lg:px-8">
        <div className="mx-auto max-w-5xl rounded-[2rem] border border-slate-200 bg-white p-6 shadow-[0_20px_60px_rgba(15,23,42,0.06)] sm:p-8">
          <div className="mb-8 flex items-center gap-3">
            <Link href="/dashboard" className="inline-flex h-10 w-10 items-center justify-center rounded-full border border-slate-200 text-slate-700">
              <ArrowLeft className="h-4 w-4" />
            </Link>
            <div>
              <p className="text-sm uppercase tracking-[0.18em] text-slate-500">Interview setup</p>
              <h1 className="text-3xl font-bold text-slate-900">Customize your mock interview</h1>
            </div>
          </div>

          <div className="grid gap-8 lg:grid-cols-[1fr_0.8fr]">
            <div className="space-y-8">
              <section>
                <h2 className="mb-4 text-lg font-semibold text-slate-900">Job role</h2>
                <div className="grid gap-3 sm:grid-cols-2 xl:grid-cols-3">
                  {jobRoles.map((role) => (
                    <button
                      key={role}
                      type="button"
                      onClick={() => setSelectedRole(role)}
                      className={`rounded-2xl border px-3 py-3 text-left text-sm font-medium transition ${selectedRole === role ? "border-slate-900 bg-slate-900 text-white" : "border-slate-200 bg-slate-50 text-slate-700 hover:border-slate-300"}`}
                    >
                      {role}
                    </button>
                  ))}
                </div>
              </section>

              <section>
                <h2 className="mb-4 text-lg font-semibold text-slate-900">Interview type</h2>
                <div className="flex flex-wrap gap-3">
                  {interviewTypes.map((type) => (
                    <button
                      key={type}
                      type="button"
                      onClick={() => setSelectedType(type)}
                      className={`rounded-full border px-4 py-2.5 text-sm font-medium transition ${selectedType === type ? "border-slate-900 bg-slate-900 text-white" : "border-slate-200 bg-slate-50 text-slate-700 hover:border-slate-300"}`}
                    >
                      {type}
                    </button>
                  ))}
                </div>
              </section>

              <section>
                <h2 className="mb-4 text-lg font-semibold text-slate-900">Difficulty</h2>
                <div className="flex flex-wrap gap-3">
                  {difficulties.map((difficulty) => (
                    <button
                      key={difficulty}
                      type="button"
                      onClick={() => setSelectedDifficulty(difficulty)}
                      className={`rounded-full border px-4 py-2.5 text-sm font-medium transition ${selectedDifficulty === difficulty ? "border-slate-900 bg-slate-900 text-white" : "border-slate-200 bg-slate-50 text-slate-700 hover:border-slate-300"}`}
                    >
                      {difficulty}
                    </button>
                  ))}
                </div>
              </section>
            </div>

            <aside className="rounded-[2rem] border border-slate-200 bg-slate-50 p-5">
              <p className="text-sm uppercase tracking-[0.18em] text-slate-500">Summary</p>
              <h2 className="mt-4 text-2xl font-bold text-slate-900">Interview preview</h2>

              <div className="mt-6 space-y-4 text-sm text-slate-600">
                <div className="rounded-2xl bg-white p-4">
                  <p className="text-slate-500">Role</p>
                  <p className="mt-1 text-base font-semibold text-slate-900">{selectedRole}</p>
                </div>
                <div className="rounded-2xl bg-white p-4">
                  <p className="text-slate-500">Type</p>
                  <p className="mt-1 text-base font-semibold text-slate-900">{selectedType}</p>
                </div>
                <div className="rounded-2xl bg-white p-4">
                  <p className="text-slate-500">Difficulty</p>
                  <p className="mt-1 text-base font-semibold text-slate-900">{selectedDifficulty}</p>
                </div>
              </div>

              <Button
                type="button"
                onClick={startInterview}
                className="mt-8 w-full rounded-full bg-slate-900 py-3 text-base font-medium text-white hover:bg-slate-800"
              >
                Start Interview
              </Button>
            </aside>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-50 px-4 py-6 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-5xl">
        <div className="mb-6 rounded-[2rem] border border-slate-200 bg-white p-4 shadow-sm sm:p-5">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
            <div>
              <p className="text-sm uppercase tracking-[0.18em] text-slate-500">{selectedRole}</p>
              <h1 className="mt-1 text-2xl font-bold text-slate-900">{selectedType} Interview</h1>
            </div>

            <Link href="/dashboard" className="inline-flex items-center gap-2 text-sm font-medium text-slate-700 hover:text-slate-900">
              <ArrowLeft className="h-4 w-4" />
              Exit Interview
            </Link>
          </div>

          <div className="mt-5">
            <div className="mb-2 flex items-center justify-between text-sm text-slate-500">
              <span>Progress</span>
              <span>{currentQuestion + 1}/{questions.length}</span>
            </div>
            <div className="h-2.5 overflow-hidden rounded-full bg-slate-200">
              <div className="h-full rounded-full bg-slate-900 transition-all" style={{ width: `${completion}%` }} />
            </div>
          </div>
        </div>

        {error ? (
          <div className="mb-4 rounded-xl border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700" role="alert">
            {error}
          </div>
        ) : null}

        <QuestionCard
          questionNumber={currentQuestion + 1}
          totalQuestions={questions.length}
          question={questions[currentQuestion]}
          answer={activeAnswer}
          onChange={handleAnswerChange}
          onSubmit={handleSubmitAnswer}
          isSubmitting={isSubmitting}
          disabled={!activeAnswer.trim() || isSubmitting}
        />

        <div className="mt-4 flex items-center justify-center gap-2 text-sm text-slate-500">
          {isSubmitting ? (
            <>
              <LoaderCircle className="h-4 w-4 animate-spin" />
              Evaluating answer...
            </>
          ) : (
            <>
              <CheckCircle2 className="h-4 w-4 text-emerald-500" />
              Ready for the next question
            </>
          )}
        </div>
      </div>
    </div>
  );
}

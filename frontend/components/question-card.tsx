import { motion } from "framer-motion";

interface QuestionCardProps {
  questionNumber: number;
  totalQuestions: number;
  question: string;
  answer: string;
  onChange: (value: string) => void;
  onSubmit: () => void;
  isSubmitting?: boolean;
  disabled?: boolean;
}

export function QuestionCard({
  questionNumber,
  totalQuestions,
  question,
  answer,
  onChange,
  onSubmit,
  isSubmitting = false,
  disabled = false,
}: QuestionCardProps) {
  return (
    <motion.div
      key={questionNumber}
      initial={{ opacity: 0, y: 18 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.25 }}
      className="rounded-3xl border border-slate-200 bg-white p-6 shadow-sm"
    >
      <div className="mb-5 flex items-center justify-between gap-4">
        <div>
          <p className="text-xs font-semibold uppercase tracking-[0.18em] text-slate-500">Question {questionNumber}</p>
          <h2 className="mt-2 text-2xl font-semibold text-slate-900">{question}</h2>
        </div>
        <span className="rounded-full bg-slate-100 px-2.5 py-1 text-xs font-medium text-slate-600">
          {questionNumber}/{totalQuestions}
        </span>
      </div>

      <label htmlFor="answer" className="mb-2 block text-sm font-medium text-slate-700">
        Your answer
      </label>
      <textarea
        id="answer"
        value={answer}
        onChange={(event) => onChange(event.target.value)}
        className="min-h-[220px] w-full rounded-2xl border border-slate-200 bg-slate-50 p-4 text-sm text-slate-700 outline-none transition focus:border-slate-300 focus:ring-3 focus:ring-slate-100"
        placeholder="Share your thought process, trade-offs, and examples..."
        aria-label="Interview response"
      />

      <div className="mt-4 flex items-center justify-between">
        <span className="text-xs text-slate-500">{answer.length} characters</span>
        <button
          type="button"
          onClick={onSubmit}
          disabled={disabled || isSubmitting}
          className="rounded-full bg-slate-900 px-5 py-2.5 text-sm font-medium text-white transition hover:bg-slate-800 disabled:cursor-not-allowed disabled:opacity-50"
        >
          {isSubmitting ? "Submitting..." : "Submit Answer"}
        </button>
      </div>
    </motion.div>
  );
}

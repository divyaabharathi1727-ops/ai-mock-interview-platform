import Link from "next/link";
import { ArrowUpRight, CalendarDays, ChevronRight, Search } from "lucide-react";
import { type Interview } from "@/types";

const interviews: Interview[] = [
  { id: "1", jobRole: "Frontend Developer", type: "Technical", difficulty: "Medium", status: "Completed", score: 89, date: "Jun 28" },
  { id: "2", jobRole: "Backend Developer", type: "Behavioral", difficulty: "Hard", status: "Completed", score: 81, date: "Jun 24" },
  { id: "3", jobRole: "Full Stack Developer", type: "Mixed", difficulty: "Hard", status: "Completed", score: 76, date: "Jun 17" },
  { id: "4", jobRole: "Data Analyst", type: "Technical", difficulty: "Easy", status: "In Progress", score: 72, date: "Jun 08" },
];

export default function HistoryPage() {
  return (
    <div className="min-h-screen bg-slate-50 px-4 py-10 sm:px-6 lg:px-8">
      <div className="mx-auto max-w-6xl">
        <div className="mb-8 flex flex-col gap-4 md:flex-row md:items-center md:justify-between">
          <div>
            <p className="text-sm uppercase tracking-[0.18em] text-slate-500">Interview history</p>
            <h1 className="mt-2 text-3xl font-bold text-slate-900">Previous interviews</h1>
          </div>

          <div className="flex items-center gap-3 rounded-full border border-slate-200 bg-white px-3 py-2 shadow-sm">
            <Search className="h-4 w-4 text-slate-400" />
            <input
              type="text"
              aria-label="Search interviews"
              placeholder="Search role or type"
              className="w-40 bg-transparent text-sm text-slate-700 outline-none placeholder:text-slate-400"
            />
          </div>
        </div>

        <div className="hidden overflow-hidden rounded-[2rem] border border-slate-200 bg-white shadow-sm lg:block">
          <table className="min-w-full divide-y divide-slate-200 text-left">
            <thead className="bg-slate-50">
              <tr className="text-sm text-slate-500">
                <th className="px-6 py-4 font-medium">Role</th>
                <th className="px-6 py-4 font-medium">Type</th>
                <th className="px-6 py-4 font-medium">Difficulty</th>
                <th className="px-6 py-4 font-medium">Score</th>
                <th className="px-6 py-4 font-medium">Date</th>
                <th className="px-6 py-4 font-medium">Status</th>
                <th className="px-6 py-4 font-medium">Action</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-200 text-sm text-slate-700">
              {interviews.map((interview) => (
                <tr key={interview.id} className="hover:bg-slate-50/80">
                  <td className="px-6 py-4 font-semibold text-slate-900">{interview.jobRole}</td>
                  <td className="px-6 py-4">{interview.type}</td>
                  <td className="px-6 py-4">{interview.difficulty}</td>
                  <td className="px-6 py-4 font-semibold text-slate-900">{interview.score}%</td>
                  <td className="px-6 py-4">{interview.date}</td>
                  <td className="px-6 py-4">
                    <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${interview.status === "Completed" ? "bg-emerald-50 text-emerald-700" : "bg-amber-50 text-amber-700"}`}>
                      {interview.status}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <Link href="/results" className="inline-flex items-center gap-2 font-semibold text-slate-900 hover:text-slate-700">
                      View Result
                      <ArrowUpRight className="h-4 w-4" />
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        <div className="space-y-4 lg:hidden">
          {interviews.map((interview) => (
            <div key={interview.id} className="rounded-[2rem] border border-slate-200 bg-white p-4 shadow-sm">
              <div className="mb-3 flex items-center justify-between gap-3">
                <div>
                  <p className="text-xs uppercase tracking-[0.18em] text-slate-500">{interview.type}</p>
                  <h2 className="mt-1 text-lg font-bold text-slate-900">{interview.jobRole}</h2>
                </div>
                <span className={`rounded-full px-2.5 py-1 text-xs font-medium ${interview.status === "Completed" ? "bg-emerald-50 text-emerald-700" : "bg-amber-50 text-amber-700"}`}>
                  {interview.status}
                </span>
              </div>

              <div className="grid grid-cols-2 gap-3 text-sm text-slate-600">
                <div>
                  <p className="text-slate-500">Difficulty</p>
                  <p className="mt-1 font-medium text-slate-900">{interview.difficulty}</p>
                </div>
                <div>
                  <p className="text-slate-500">Score</p>
                  <p className="mt-1 font-medium text-slate-900">{interview.score}%</p>
                </div>
                <div className="col-span-2 flex items-center gap-2">
                  <CalendarDays className="h-4 w-4 text-slate-400" />
                  {interview.date}
                </div>
              </div>

              <Link href="/results" className="mt-4 inline-flex items-center gap-2 text-sm font-semibold text-slate-900">
                View result
                <ChevronRight className="h-4 w-4" />
              </Link>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

export type InterviewType = "Technical" | "Behavioral" | "Mixed";
export type Difficulty = "Easy" | "Medium" | "Hard";
export type InterviewStatus = "Completed" | "In Progress" | "Scheduled";

export interface User {
  id: string;
  name: string;
  email: string;
  role?: string;
  createdAt: string;
}

export interface Interview {
  id: string;
  jobRole: string;
  type: InterviewType;
  difficulty: Difficulty;
  status: InterviewStatus;
  score: number;
  date: string;
}

export interface Question {
  id: string;
  text: string;
  category: "Technical" | "Communication" | "Problem Solving";
  skill: string;
}

export interface Answer {
  questionId: string;
  body: string;
  submittedAt: string;
}

export interface InterviewResult {
  overall: number;
  technical: number;
  communication: number;
  relevance: number;
  completeness: number;
  strengths: string[];
  weaknesses: string[];
  feedback: string;
  improvements: string[];
}

export interface DashboardStats {
  totalInterviews: number;
  averageScore: number;
  bestScore: number;
  latestScore: number;
  completionRate: number;
}

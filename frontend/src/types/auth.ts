export type UserRole = "STUDENT" | "INTERVIEWER" | "ADMIN" | string;

export interface User {
  id: number;
  name: string;
  email: string;
  role: UserRole;
}

export interface AuthResponse {
  token: string;
  user: User;
}

export interface ApiErrorResponse {
  status?: number;
  message?: string;
}

export type InterviewDifficulty = "EASY" | "MEDIUM" | "HARD";
export type InterviewStatus = "CREATED" | "IN_PROGRESS" | "COMPLETED" | "CANCELLED";

export interface Interview {
  id: number;
  jobRole: string;
  interviewType: string;
  difficulty: InterviewDifficulty;
  status: InterviewStatus;
  createdAt: string;
  updatedAt: string;
}

export interface InterviewCreateRequest {
  jobRole: string;
  interviewType: string;
  difficulty: InterviewDifficulty;
}

export interface InterviewUpdateRequest extends InterviewCreateRequest {
  status: InterviewStatus;
}

export type InterviewQuestionType = "TEXT" | "MCQ" | "MULTIPLE_SELECT" | "TRUE_FALSE" | "TECHNICAL" | "BEHAVIORAL" | "HR" | "SCENARIO";

export interface InterviewQuestion {
  id: number;
  questionText: string;
  questionOrder: number;
  questionType: InterviewQuestionType;
  options: string[];
}

export interface InterviewSession {
  currentQuestion: InterviewQuestion | null;
  readyToFinish: boolean;
  answeredQuestions: number;
  totalQuestions: number;
}

export interface AnswerRequest {
  answerText?: string;
  selectedOption?: string;
  selectedOptions?: string[];
}

export interface AnswerResponse {
  nextQuestion: InterviewQuestion | null;
  readyToFinish: boolean;
  answeredQuestions: number;
  totalQuestions: number;
}

export interface InterviewCompletion {
  status: InterviewStatus;
  totalQuestions: number;
  answeredQuestions: number;
  completedAt: string;
}

export interface InterviewResults {
  id: number;
  jobRole: string;
  interviewType: string;
  difficulty: InterviewDifficulty;
  status: InterviewStatus;
  totalQuestions: number;
  answeredQuestions: number;
  completedAt: string | null;
  overallScore: number | null;
  technicalScore: number | null;
  relevanceScore: number | null;
  clarityScore: number | null;
  completenessScore: number | null;
  questions: QuestionResult[];
  durationSeconds: number | null;
  correctObjectiveAnswers: number;
  incorrectObjectiveAnswers: number;
  unansweredQuestions: number;
}

export interface AnswerEvaluation {
  available: boolean;
  correct: boolean | null;
  overallScore: number | null;
  technicalScore: number | null;
  relevanceScore: number | null;
  clarityScore: number | null;
  completenessScore: number | null;
  feedback: string | null;
  strengths: string[];
  improvements: string[];
  weaknesses: string[];
  improvementSuggestion: string | null;
  idealAnswerGuidance: string | null;
}

export interface QuestionResult {
  questionId: number;
  questionText: string;
  questionType: InterviewQuestionType;
  options: string[];
  candidateAnswer: string | null;
  evaluation: AnswerEvaluation | null;
  status: "CORRECT" | "INCORRECT" | "UNANSWERED" | "EVALUATED" | "EVALUATION_UNAVAILABLE";
  correctAnswer?: string | null;
}

export interface InterviewAnalytics {
  totalInterviews: number;
  completedInterviews: number;
  inProgressInterviews: number;
  averageScore: number | null;
  bestScore: number | null;
  averageDurationSeconds: number | null;
  totalQuestions: number;
  totalAnsweredQuestions: number;
  totalCorrectObjectiveAnswers: number;
  totalIncorrectObjectiveAnswers: number;
  trend: { label: string; score: number | null; durationSeconds: number | null }[];
  strongAreas: string[];
  practiceRecommendations: string[];
}

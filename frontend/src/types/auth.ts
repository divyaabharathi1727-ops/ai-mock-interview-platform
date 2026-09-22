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

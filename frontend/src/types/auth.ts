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

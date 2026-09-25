import type { Metadata } from "next";
import { AuthProvider } from "@/components/AuthProvider";
import { ThemeProvider } from "@/components/ThemeProvider";
import { ThemeToggle } from "@/components/ThemeToggle";
import "./globals.css";

export const metadata: Metadata = {
  title: "Interview Lab",
  description: "Practice interviews with clarity and confidence.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return (
    <html lang="en">
      <body><ThemeProvider><AuthProvider><ThemeToggle /><div className="app-content">{children}</div></AuthProvider></ThemeProvider></body>
    </html>
  );
}

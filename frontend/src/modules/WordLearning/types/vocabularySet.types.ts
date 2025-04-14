export type VocabularySetType = {
  id: string;
  name: string;
  image: string;
  description: string;
  createdBy: string;
  tags: string; // Changed from string[]
  visibility: "PRIVATE" | "PUBLIC"; // Using literal types
  category: "GENERAL" | string;
  difficultyLevel: "BEGINNER" | "INTERMEDIATE" | "ADVANCED";
  itemCount: number; // Replaces numberOfWords
  viewCount: number;
  likeCount: number;
  shareCount: number;
  createdAt: Date;
  updatedAt: Date;
  system: boolean;
  featured: boolean;
};

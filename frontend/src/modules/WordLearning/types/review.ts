export interface Review {
  id: string;
  packageId: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  rating: number;
  comment: string;
  date: string;
  likes: number;
  isVerifiedLearner: boolean;
  isAuthor: boolean;
  replies?: ReviewReply[];
  replyCount: number;
  isEdited: boolean;
  lastEditedAt?: string;
  media?: ReviewMedia[];
  helpfulCount: number;
  reportCount: number;
  status: ReviewStatus;
  userHasLiked: boolean;
  userHasReported: boolean;
}

export interface ReviewReply {
  id: string;
  reviewId: string;
  userId: string;
  userName: string;
  userAvatar?: string;
  comment: string;
  date: string;
  likes: number;
  isAuthor: boolean;
  isVerifiedLearner: boolean;
  isEdited: boolean;
  lastEditedAt?: string;
  parentReplyId?: string;
  userHasLiked: boolean;
}

export interface ReviewMedia {
  id: string;
  type: 'image' | 'video';
  url: string;
  thumbnailUrl?: string;
  caption?: string;
}

export type ReviewStatus = 'published' | 'pending' | 'hidden' | 'reported' | 'deleted';

export interface ReviewFilters {
  rating?: number;
  verified?: boolean;
  hasMedia?: boolean;
  dateRange?: {
    start: string;
    end: string;
  };
  sortBy: ReviewSortOption;
  sortOrder: 'asc' | 'desc';
}

export type ReviewSortOption = 
  | 'date'
  | 'rating'
  | 'helpful'
  | 'replies'
  | 'likes';

export interface ReviewStats {
  averageRating: number;
  totalReviews: number;
  ratingDistribution: { [key: number]: number };
  verifiedReviews: number;
  withMediaCount: number;
  responseRate: number;
  averageResponseTime: number;
  recentTrend: {
    period: string;
    averageRating: number;
    totalReviews: number;
  }[];
} 
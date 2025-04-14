import { useState, useCallback, useEffect } from 'react';
import reviewService from '../services/reviewService';
import { Review } from '../types/review';

interface UseReviewsProps {
  packageId: string;
}

interface ReviewStats {
  averageRating: number;
  totalReviews: number;
  ratingDistribution: { [key: number]: number };
}

export const useReviews = ({ packageId }: UseReviewsProps) => {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [stats, setStats] = useState<ReviewStats | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);

  // Fetch reviews
  const fetchReviews = useCallback(async (pageNum = 1) => {
    try {
      setLoading(true);
      setError(null);
      const data = await reviewService.getPackageReviews(packageId, pageNum);
      
      if (pageNum === 1) {
        setReviews(data.reviews);
      } else {
        setReviews(prev => [...prev, ...data.reviews]);
      }
      
      setHasMore(data.hasMore);
      setPage(pageNum);
    } catch (err) {
      setError('Failed to load reviews');
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [packageId]);

  // Fetch review statistics
  const fetchStats = useCallback(async () => {
    try {
      const data = await reviewService.getReviewStats(packageId);
      setStats(data);
    } catch (err) {
      console.error('Failed to load review statistics:', err);
    }
  }, [packageId]);

  // Initial load
  useEffect(() => {
    fetchReviews(1);
    fetchStats();
  }, [fetchReviews, fetchStats]);

  // Add review
  const addReview = async (rating: number, comment: string) => {
    try {
      setLoading(true);
      await reviewService.createReview({
        packageId,
        rating,
        comment
      });
      
      // Refresh reviews and stats
      await Promise.all([
        fetchReviews(1),
        fetchStats()
      ]);
    } catch (err) {
      setError('Failed to add review');
      throw err;
    } finally {
      setLoading(false);
    }
  };

  // Like review
  const likeReview = async (reviewId: string) => {
    try {
      await reviewService.likeReview(reviewId);
      setReviews(prev =>
        prev.map(review =>
          review.id === reviewId
            ? { ...review, likes: review.likes + 1 }
            : review
        )
      );
    } catch (err) {
      setError('Failed to like review');
      throw err;
    }
  };

  // Add reply
  const addReply = async (reviewId: string, comment: string) => {
    try {
      const newReply = await reviewService.createReply({
        reviewId,
        comment
      });
      
      setReviews(prev =>
        prev.map(review =>
          review.id === reviewId
            ? {
                ...review,
                replies: [...(review.replies || []), newReply],
                replyCount: (review.replyCount || 0) + 1
              }
            : review
        )
      );
    } catch (err) {
      setError('Failed to add reply');
      throw err;
    }
  };

  // Delete review
  const deleteReview = async (reviewId: string) => {
    try {
      await reviewService.deleteReview(reviewId);
      setReviews(prev => prev.filter(review => review.id !== reviewId));
      await fetchStats();
    } catch (err) {
      setError('Failed to delete review');
      throw err;
    }
  };

  // Update review
  const updateReview = async (reviewId: string, data: { rating?: number; comment?: string }) => {
    try {
      const updatedReview = await reviewService.updateReview(reviewId, data);
      setReviews(prev =>
        prev.map(review =>
          review.id === reviewId
            ? { ...review, ...updatedReview }
            : review
        )
      );
      await fetchStats();
    } catch (err) {
      setError('Failed to update review');
      throw err;
    }
  };

  // Load more reviews
  const loadMore = async () => {
    if (!hasMore || loading) return;
    await fetchReviews(page + 1);
  };

  return {
    reviews,
    stats,
    loading,
    error,
    hasMore,
    addReview,
    likeReview,
    addReply,
    deleteReview,
    updateReview,
    loadMore,
    refreshReviews: () => fetchReviews(1)
  };
};

export default useReviews; 
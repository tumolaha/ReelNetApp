import { FiStar, FiThumbsUp, FiMessageSquare, FiX } from "react-icons/fi";
import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";

interface ReviewsVocabularyPackageProps {
  reviews?: {
    averageRating: number;
    totalReviews: number;
    ratingDistribution: { [key: number]: number };
    reviews: Review[];
  };
  onAddReview?: (review: Omit<Review, "id" | "likes" | "replies" | "date">) => void;
  onLikeReview?: (reviewId: string) => void;
  onReplyReview?: (reviewId: string, comment: string) => void;
}

interface Review {
  id: string;
  userName: string;
  userAvatar?: string;
  rating: number;
  comment: string;
  date: string;
  likes: number;
  replies: number;
  isVerifiedLearner: boolean;
}

const ReviewsVocabularyPackage: React.FC<ReviewsVocabularyPackageProps> = ({
  reviews = {
    averageRating: 4.8,
    totalReviews: 256,
    ratingDistribution: {
      5: 180,
      4: 50,
      3: 15,
      2: 8,
      1: 3,
    },
    reviews: [
      {
        id: "1",
        userName: "John Smith",
        rating: 5,
        comment:
          "Excellent vocabulary package! The words are well-chosen and the examples are very practical. I've improved my TOEIC score significantly after studying this package.",
        date: "2024-03-15",
        likes: 24,
        replies: 3,
        isVerifiedLearner: true,
      },
      {
        id: "2",
        userName: "Emma Wilson",
        rating: 4,
        comment:
          "Very comprehensive package. The only thing I would suggest is adding more business-specific examples.",
        date: "2024-03-14",
        likes: 15,
        replies: 1,
        isVerifiedLearner: true,
      },
      {
        id: "3",
        userName: "David Chen",
        rating: 5,
        comment:
          "The spaced repetition system really helps with memorization. Great selection of words and excellent audio pronunciation.",
        date: "2024-03-13",
        likes: 19,
        replies: 2,
        isVerifiedLearner: true,
      },
    ],
  },
  onAddReview,
  onLikeReview,
  onReplyReview,
}) => {
  const [sortBy, setSortBy] = useState<"recent" | "rating" | "helpful">("recent");
  const [isWritingReview, setIsWritingReview] = useState(false);
  const [newReview, setNewReview] = useState({
    rating: 5,
    comment: "",
  });
  const [replyingTo, setReplyingTo] = useState<string | null>(null);
  const [replyComment, setReplyComment] = useState("");
  const [hoveredRating, setHoveredRating] = useState<number | null>(null);
  const [likedReviews, setLikedReviews] = useState<Set<string>>(new Set());

  const handleRatingHover = (rating: number | null) => {
    setHoveredRating(rating);
  };

  const handleRatingClick = (rating: number) => {
    setNewReview(prev => ({ ...prev, rating }));
  };

  const handleSubmitReview = () => {
    if (!newReview.comment.trim()) {
      // Show error or validation message
      return;
    }

    onAddReview?.({
      userName: "Current User", // This should come from auth context
      rating: newReview.rating,
      comment: newReview.comment.trim(),
      isVerifiedLearner: true,
    });

    setNewReview({ rating: 5, comment: "" });
    setIsWritingReview(false);
  };

  const handleLikeReview = (reviewId: string) => {
    if (likedReviews.has(reviewId)) {
      return;
    }
    
    setLikedReviews(prev => new Set([...prev, reviewId]));
    onLikeReview?.(reviewId);
  };

  const handleSubmitReply = (reviewId: string) => {
    if (!replyComment.trim()) return;

    onReplyReview?.(reviewId, replyComment);
    setReplyComment("");
    setReplyingTo(null);
  };

  const renderStars = (rating: number, interactive = false) => {
    return [...Array(5)].map((_, index) => (
      <FiStar
        key={index}
        className={`w-4 h-4 ${
          index < (hoveredRating ?? rating) 
            ? "text-yellow-400 fill-yellow-400" 
            : "text-gray-400"
        } ${interactive ? "cursor-pointer" : ""}`}
        onMouseEnter={interactive ? () => handleRatingHover(index + 1) : undefined}
        onMouseLeave={interactive ? () => handleRatingHover(null) : undefined}
        onClick={interactive ? () => handleRatingClick(index + 1) : undefined}
      />
    ));
  };

  const calculatePercentage = (count: number) => {
    return ((count / reviews.totalReviews) * 100).toFixed(0);
  };

  return (
    <div className="bg-[#0F172A] rounded-xl shadow-xl">
      {/* Header */}
      <div className="bg-gradient-to-br from-blue-900 to-[#1E293B] p-6 rounded-t-xl border-b border-blue-800/30">
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-2xl font-bold text-white">Reviews</h2>
          <div className="flex items-center gap-2">
            <span className="text-3xl font-bold text-white">
              {reviews.averageRating}
            </span>
            <div className="flex flex-col">
              <div className="flex items-center gap-1">
                {renderStars(Math.round(reviews.averageRating))}
              </div>
              <span className="text-blue-300/70 text-sm">
                {reviews.totalReviews} reviews
              </span>
            </div>
          </div>
        </div>

        {/* Rating Distribution */}
        <div className="space-y-2">
          {[5, 4, 3, 2, 1].map((star) => (
            <div key={star} className="flex items-center gap-3">
              <div className="flex items-center gap-1 w-20">
                <span className="text-sm text-blue-300/70">{star}</span>
                <FiStar className="w-4 h-4 text-yellow-400" />
              </div>
              <div className="flex-1 h-2 bg-blue-950/50 rounded-full overflow-hidden">
                <div
                  className="h-full bg-yellow-400 rounded-full"
                  style={{
                    width: `${calculatePercentage(
                      reviews.ratingDistribution[star]
                    )}%`,
                  }}
                />
              </div>
              <span className="text-sm text-blue-300/70 w-16">
                {calculatePercentage(reviews.ratingDistribution[star])}%
              </span>
            </div>
          ))}
        </div>
      </div>

      {/* Review Controls */}
      <div className="p-4 border-b border-blue-800/30">
        <div className="flex items-center justify-between">
          <button 
            onClick={() => setIsWritingReview(true)}
            className="px-4 py-2 bg-blue-600 hover:bg-blue-700 text-white rounded-lg font-medium transition-colors"
          >
            Write a Review
          </button>
          <div className="flex items-center gap-2">
            <span className="text-blue-300/70">Sort by:</span>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value as any)}
              className="bg-blue-950/50 text-blue-200 border border-blue-800/30 rounded-lg px-3 py-1.5 outline-none focus:ring-2 focus:ring-blue-500/50"
            >
              <option value="recent">Most Recent</option>
              <option value="rating">Highest Rating</option>
              <option value="helpful">Most Helpful</option>
            </select>
          </div>
        </div>
      </div>

      {/* Reviews List */}
      <div className="p-4">
        <div className="space-y-6">
          {reviews.reviews.map((review) => (
            <div
              key={review.id}
              className="p-4 bg-blue-950/20 rounded-lg border border-blue-800/20"
            >
              <div className="flex items-start justify-between mb-3">
                <div className="flex items-center gap-3">
                  <div className="w-10 h-10 rounded-full bg-blue-950/50 border border-blue-800/30 flex items-center justify-center">
                    {review.userAvatar ? (
                      <img
                        src={review.userAvatar}
                        alt={review.userName}
                        className="w-full h-full rounded-full object-cover"
                      />
                    ) : (
                      <span className="text-blue-300 font-medium">
                        {review.userName.charAt(0)}
                      </span>
                    )}
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="font-medium text-blue-100">
                        {review.userName}
                      </span>
                      {review.isVerifiedLearner && (
                        <span className="text-xs bg-green-500/20 text-green-400 px-2 py-0.5 rounded-full">
                          Verified Learner
                        </span>
                      )}
                    </div>
                    <div className="flex items-center gap-2 text-sm text-blue-300/70">
                      <div className="flex items-center gap-1">
                        {renderStars(review.rating)}
                      </div>
                      <span>•</span>
                      <span>{new Date(review.date).toLocaleDateString()}</span>
                    </div>
                  </div>
                </div>
              </div>

              <p className="text-blue-200 mb-4">{review.comment}</p>

              <div className="flex items-center gap-4">
                <button 
                  className={`flex items-center gap-2 ${
                    likedReviews.has(review.id)
                      ? "text-blue-400"
                      : "text-blue-300/70 hover:text-blue-200"
                  } transition-colors`}
                  onClick={() => handleLikeReview(review.id)}
                  disabled={likedReviews.has(review.id)}
                >
                  <FiThumbsUp className="w-4 h-4" />
                  <span className="text-sm">{review.likes + (likedReviews.has(review.id) ? 1 : 0)}</span>
                </button>
                <button 
                  className="flex items-center gap-2 text-blue-300/70 hover:text-blue-200 transition-colors"
                  onClick={() => setReplyingTo(review.id)}
                >
                  <FiMessageSquare className="w-4 h-4" />
                  <span className="text-sm">{review.replies}</span>
                </button>
              </div>

              {/* Reply Section */}
              {replyingTo === review.id && (
                <div className="mt-4 pt-4 border-t border-blue-800/20">
                  <textarea
                    value={replyComment}
                    onChange={(e) => setReplyComment(e.target.value)}
                    placeholder="Write your reply..."
                    className="w-full bg-blue-950/30 text-blue-200 rounded-lg border border-blue-800/30 p-3 min-h-[100px] outline-none focus:ring-2 focus:ring-blue-500/50"
                  />
                  <div className="flex justify-end gap-2 mt-2">
                    <button
                      onClick={() => setReplyingTo(null)}
                      className="px-3 py-1.5 text-blue-300/70 hover:text-blue-200 transition-colors"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={() => handleSubmitReply(review.id)}
                      className="px-3 py-1.5 bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors"
                    >
                      Submit Reply
                    </button>
                  </div>
                </div>
              )}
            </div>
          ))}
        </div>
      </div>

      {/* Write Review Modal */}
      <AnimatePresence>
        {isWritingReview && (
          <div className="fixed inset-0 bg-black/50 flex items-center justify-center p-4 z-50">
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.9 }}
              className="bg-[#0F172A] rounded-xl shadow-xl max-w-2xl w-full"
            >
              <div className="p-6 border-b border-blue-800/30">
                <div className="flex items-center justify-between">
                  <h3 className="text-xl font-bold text-white">Write a Review</h3>
                  <button
                    onClick={() => setIsWritingReview(false)}
                    className="text-blue-300/70 hover:text-blue-200 transition-colors"
                  >
                    <FiX className="w-6 h-6" />
                  </button>
                </div>
              </div>

              <div className="p-6">
                <div className="mb-6">
                  <label className="block text-blue-200 mb-2">Rating</label>
                  <div className="flex items-center gap-1">
                    {renderStars(newReview.rating, true)}
                  </div>
                </div>

                <div className="mb-6">
                  <label className="block text-blue-200 mb-2">Your Review</label>
                  <textarea
                    value={newReview.comment}
                    onChange={(e) => setNewReview(prev => ({ ...prev, comment: e.target.value }))}
                    placeholder="Share your experience with this vocabulary package..."
                    className="w-full bg-blue-950/30 text-blue-200 rounded-lg border border-blue-800/30 p-4 min-h-[150px] outline-none focus:ring-2 focus:ring-blue-500/50"
                  />
                </div>

                <div className="flex justify-end gap-3">
                  <button
                    onClick={() => setIsWritingReview(false)}
                    className="px-4 py-2 text-blue-300/70 hover:text-blue-200 transition-colors"
                  >
                    Cancel
                  </button>
                  <button
                    onClick={handleSubmitReview}
                    disabled={!newReview.comment.trim()}
                    className="px-6 py-2 bg-blue-600 hover:bg-blue-700 disabled:bg-blue-900 disabled:cursor-not-allowed text-white rounded-lg font-medium transition-colors"
                  >
                    Submit Review
                  </button>
                </div>
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>

      {/* Footer */}
      <div className="p-4 border-t border-blue-800/30">
        <button className="w-full py-2 text-center text-blue-300/70 hover:text-blue-200 font-medium transition-colors">
          Load More Reviews
        </button>
      </div>
    </div>
  );
};

export default ReviewsVocabularyPackage;

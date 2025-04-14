import { Review } from '@/modules/WordLearning/types/review';
import { ReviewStatus } from '@/modules/WordLearning/types/review';
import { useState } from 'react';
import { FiFlag, FiAlertTriangle, FiEye, FiEyeOff, FiTrash2 } from 'react-icons/fi';


interface ReviewModerationProps {
  review: Review;
  onReport: (reviewId: string, reason: string) => Promise<void>;
  onUpdateStatus: (reviewId: string, status: ReviewStatus) => Promise<void>;
  onDelete: (reviewId: string) => Promise<void>;
  isAdmin?: boolean;
}

const REPORT_REASONS = [
  'Inappropriate content',
  'Spam',
  'Offensive language',
  'Misleading information',
  'Advertising',
  'Other',
];

const ReviewModeration: React.FC<ReviewModerationProps> = ({
  review,
  onReport,
  onUpdateStatus,
  onDelete,
  isAdmin = false,
}) => {
  const [isReporting, setIsReporting] = useState(false);
  const [reportReason, setReportReason] = useState('');
  const [otherReason, setOtherReason] = useState('');
  const [loading, setLoading] = useState(false);

  const handleReport = async () => {
    if (!reportReason) return;

    try {
      setLoading(true);
      const finalReason = reportReason === 'Other' ? otherReason : reportReason;
      await onReport(review.id, finalReason);
      setIsReporting(false);
      setReportReason('');
      setOtherReason('');
    } catch (error) {
      console.error('Failed to report review:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleStatusUpdate = async (status: ReviewStatus) => {
    try {
      setLoading(true);
      await onUpdateStatus(review.id, status);
    } catch (error) {
      console.error('Failed to update review status:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Are you sure you want to delete this review?')) return;

    try {
      setLoading(true);
      await onDelete(review.id);
    } catch (error) {
      console.error('Failed to delete review:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="relative">
      {/* Report Button */}
      {!review.userHasReported && !isAdmin && (
        <button
          onClick={() => setIsReporting(true)}
          className="text-blue-300/70 hover:text-blue-200 flex items-center gap-2 text-sm"
        >
          <FiFlag className="w-4 h-4" />
          <span>Report</span>
        </button>
      )}

      {/* Report Modal */}
      {isReporting && (
        <div className="absolute right-0 top-8 w-80 bg-[#1E293B] rounded-lg shadow-xl border border-blue-800/30 p-4 z-10">
          <h4 className="text-white font-medium mb-3">Report Review</h4>
          
          <div className="space-y-3">
            <select
              value={reportReason}
              onChange={(e) => setReportReason(e.target.value)}
              className="w-full bg-blue-950/50 text-blue-200 border border-blue-800/30 rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-blue-500/50"
            >
              <option value="">Select a reason</option>
              {REPORT_REASONS.map((reason) => (
                <option key={reason} value={reason}>
                  {reason}
                </option>
              ))}
            </select>

            {reportReason === 'Other' && (
              <textarea
                value={otherReason}
                onChange={(e) => setOtherReason(e.target.value)}
                placeholder="Please specify the reason..."
                className="w-full bg-blue-950/50 text-blue-200 border border-blue-800/30 rounded-lg px-3 py-2 min-h-[80px] outline-none focus:ring-2 focus:ring-blue-500/50"
              />
            )}

            <div className="flex justify-end gap-2">
              <button
                onClick={() => setIsReporting(false)}
                className="px-3 py-1.5 text-blue-300/70 hover:text-blue-200 transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={handleReport}
                disabled={!reportReason || loading}
                className="px-3 py-1.5 bg-red-600 hover:bg-red-700 disabled:bg-red-900 text-white rounded-lg transition-colors"
              >
                Submit Report
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Admin Controls */}
      {isAdmin && (
        <div className="flex items-center gap-3">
          <div className="flex items-center gap-2 text-sm">
            <FiAlertTriangle className={`w-4 h-4 ${review.reportCount > 0 ? 'text-red-400' : 'text-gray-400'}`} />
            <span className="text-blue-300/70">{review.reportCount} reports</span>
          </div>

          <div className="flex items-center gap-2">
            <button
              onClick={() => handleStatusUpdate(review.status === 'hidden' ? 'published' : 'hidden')}
              className="text-blue-300/70 hover:text-blue-200 p-1.5 rounded-lg transition-colors"
              title={review.status === 'hidden' ? 'Show Review' : 'Hide Review'}
            >
              {review.status === 'hidden' ? (
                <FiEye className="w-4 h-4" />
              ) : (
                <FiEyeOff className="w-4 h-4" />
              )}
            </button>

            <button
              onClick={handleDelete}
              className="text-red-400 hover:text-red-300 p-1.5 rounded-lg transition-colors"
              title="Delete Review"
            >
              <FiTrash2 className="w-4 h-4" />
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ReviewModeration; 
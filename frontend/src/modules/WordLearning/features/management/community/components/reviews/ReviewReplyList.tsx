import { FiThumbsUp, FiMessageSquare } from "react-icons/fi";
import { useState } from "react";

interface ReviewReply {
  id: string;
  userName: string;
  userAvatar?: string;
  comment: string;
  date: string;
  likes: number;
  isAuthor: boolean;
  isVerifiedLearner: boolean;
}

interface ReviewReplyListProps {
  reviewId: string;
  replies: ReviewReply[];
  onLikeReply?: (replyId: string) => void;
  onAddReply?: (comment: string) => void;
  onDeleteReply?: (replyId: string) => void;
  onEditReply?: (replyId: string, newComment: string) => void;
}

const ReviewReplyList: React.FC<ReviewReplyListProps> = ({
  reviewId,
  replies,
  onLikeReply,
  onAddReply,
  onDeleteReply,
  onEditReply,
}) => {
  const [likedReplies, setLikedReplies] = useState<Set<string>>(new Set());
  const [editingReplyId, setEditingReplyId] = useState<string | null>(null);
  const [editedComment, setEditedComment] = useState("");

  const handleLikeReply = (replyId: string) => {
    if (likedReplies.has(replyId)) return;
    
    setLikedReplies(prev => new Set([...prev, replyId]));
    onLikeReply?.(replyId);
  };

  const handleEditClick = (reply: ReviewReply) => {
    setEditingReplyId(reply.id);
    setEditedComment(reply.comment);
  };

  const handleSaveEdit = (replyId: string) => {
    if (!editedComment.trim()) return;
    
    onEditReply?.(replyId, editedComment.trim());
    setEditingReplyId(null);
    setEditedComment("");
  };

  return (
    <div className="ml-8 mt-4 space-y-4">
      {replies.map((reply) => (
        <div
          key={reply.id}
          className="bg-blue-950/30 rounded-lg p-4 border border-blue-800/20"
        >
          <div className="flex items-start gap-3">
            <div className="w-8 h-8 rounded-full bg-blue-950/50 border border-blue-800/30 flex items-center justify-center flex-shrink-0">
              {reply.userAvatar ? (
                <img
                  src={reply.userAvatar}
                  alt={reply.userName}
                  className="w-full h-full rounded-full object-cover"
                />
              ) : (
                <span className="text-blue-300 font-medium text-sm">
                  {reply.userName.charAt(0)}
                </span>
              )}
            </div>

            <div className="flex-1">
              <div className="flex items-center gap-2 mb-1">
                <span className="font-medium text-blue-100">
                  {reply.userName}
                </span>
                {reply.isAuthor && (
                  <span className="text-xs bg-blue-500/20 text-blue-400 px-2 py-0.5 rounded-full">
                    Author
                  </span>
                )}
                {reply.isVerifiedLearner && (
                  <span className="text-xs bg-green-500/20 text-green-400 px-2 py-0.5 rounded-full">
                    Verified Learner
                  </span>
                )}
                <span className="text-sm text-blue-300/70">
                  • {new Date(reply.date).toLocaleDateString()}
                </span>
              </div>

              {editingReplyId === reply.id ? (
                <div className="space-y-2">
                  <textarea
                    value={editedComment}
                    onChange={(e) => setEditedComment(e.target.value)}
                    className="w-full bg-blue-950/30 text-blue-200 rounded-lg border border-blue-800/30 p-2 min-h-[80px] outline-none focus:ring-2 focus:ring-blue-500/50"
                  />
                  <div className="flex justify-end gap-2">
                    <button
                      onClick={() => setEditingReplyId(null)}
                      className="px-3 py-1 text-sm text-blue-300/70 hover:text-blue-200 transition-colors"
                    >
                      Cancel
                    </button>
                    <button
                      onClick={() => handleSaveEdit(reply.id)}
                      className="px-3 py-1 text-sm bg-blue-600 hover:bg-blue-700 text-white rounded-lg transition-colors"
                    >
                      Save
                    </button>
                  </div>
                </div>
              ) : (
                <p className="text-blue-200 text-sm mb-2">{reply.comment}</p>
              )}

              <div className="flex items-center gap-4">
                <button
                  className={`flex items-center gap-1 text-sm ${
                    likedReplies.has(reply.id)
                      ? "text-blue-400"
                      : "text-blue-300/70 hover:text-blue-200"
                  } transition-colors`}
                  onClick={() => handleLikeReply(reply.id)}
                  disabled={likedReplies.has(reply.id)}
                >
                  <FiThumbsUp className="w-3 h-3" />
                  <span>{reply.likes + (likedReplies.has(reply.id) ? 1 : 0)}</span>
                </button>

                {reply.isAuthor && (
                  <>
                    <button
                      onClick={() => handleEditClick(reply)}
                      className="text-sm text-blue-300/70 hover:text-blue-200 transition-colors"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => onDeleteReply?.(reply.id)}
                      className="text-sm text-red-400 hover:text-red-300 transition-colors"
                    >
                      Delete
                    </button>
                  </>
                )}
              </div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
};

export default ReviewReplyList; 
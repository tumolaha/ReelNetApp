import axios from 'axios';

const API_BASE_URL = '/api/vocabulary-packages';

export interface ReviewCreateDTO {
  rating: number;
  comment: string;
  packageId: string;
}

export interface ReplyCreateDTO {
  comment: string;
  reviewId: string;
}

export const reviewService = {
  // Get reviews for a package
  getPackageReviews: async (packageId: string, page = 1, limit = 10) => {
    const response = await axios.get(
      `${API_BASE_URL}/${packageId}/reviews`,
      {
        params: { page, limit }
      }
    );
    return response.data;
  },

  // Create a new review
  createReview: async (data: ReviewCreateDTO) => {
    const response = await axios.post(
      `${API_BASE_URL}/${data.packageId}/reviews`,
      data
    );
    return response.data;
  },

  // Update a review
  updateReview: async (reviewId: string, data: Partial<ReviewCreateDTO>) => {
    const response = await axios.patch(
      `${API_BASE_URL}/reviews/${reviewId}`,
      data
    );
    return response.data;
  },

  // Delete a review
  deleteReview: async (reviewId: string) => {
    const response = await axios.delete(
      `${API_BASE_URL}/reviews/${reviewId}`
    );
    return response.data;
  },

  // Like a review
  likeReview: async (reviewId: string) => {
    const response = await axios.post(
      `${API_BASE_URL}/reviews/${reviewId}/like`
    );
    return response.data;
  },

  // Unlike a review
  unlikeReview: async (reviewId: string) => {
    const response = await axios.delete(
      `${API_BASE_URL}/reviews/${reviewId}/like`
    );
    return response.data;
  },

  // Get replies for a review
  getReviewReplies: async (reviewId: string, page = 1, limit = 10) => {
    const response = await axios.get(
      `${API_BASE_URL}/reviews/${reviewId}/replies`,
      {
        params: { page, limit }
      }
    );
    return response.data;
  },

  // Create a reply
  createReply: async (data: ReplyCreateDTO) => {
    const response = await axios.post(
      `${API_BASE_URL}/reviews/${data.reviewId}/replies`,
      data
    );
    return response.data;
  },

  // Update a reply
  updateReply: async (replyId: string, comment: string) => {
    const response = await axios.patch(
      `${API_BASE_URL}/replies/${replyId}`,
      { comment }
    );
    return response.data;
  },

  // Delete a reply
  deleteReply: async (replyId: string) => {
    const response = await axios.delete(
      `${API_BASE_URL}/replies/${replyId}`
    );
    return response.data;
  },

  // Like a reply
  likeReply: async (replyId: string) => {
    const response = await axios.post(
      `${API_BASE_URL}/replies/${replyId}/like`
    );
    return response.data;
  },

  // Get review statistics
  getReviewStats: async (packageId: string) => {
    const response = await axios.get(
      `${API_BASE_URL}/${packageId}/reviews/stats`
    );
    return response.data;
  },
};

export default reviewService; 
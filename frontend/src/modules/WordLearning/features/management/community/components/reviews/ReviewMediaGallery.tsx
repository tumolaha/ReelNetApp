import { useState } from 'react';
import { motion, AnimatePresence } from 'framer-motion';
import { FiX, FiMaximize2, FiChevronLeft, FiChevronRight } from 'react-icons/fi';
import { ReviewMedia } from '@/modules/WordLearning/types/review';

interface ReviewMediaGalleryProps {
  media: ReviewMedia[];
  onDelete?: (mediaId: string) => void;
}

const ReviewMediaGallery: React.FC<ReviewMediaGalleryProps> = ({
  media,
  onDelete,
}) => {
  const [selectedMedia, setSelectedMedia] = useState<ReviewMedia | null>(null);
  const [currentIndex, setCurrentIndex] = useState(0);

  const handlePrevious = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (currentIndex > 0) {
      setCurrentIndex(currentIndex - 1);
      setSelectedMedia(media[currentIndex - 1]);
    }
  };

  const handleNext = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (currentIndex < media.length - 1) {
      setCurrentIndex(currentIndex + 1);
      setSelectedMedia(media[currentIndex + 1]);
    }
  };

  return (
    <div className="space-y-4">
      <div className="grid grid-cols-2 sm:grid-cols-3 gap-2">
        {media.map((item, index) => (
          <div
            key={item.id}
            className="relative aspect-square rounded-lg overflow-hidden group cursor-pointer"
            onClick={() => {
              setSelectedMedia(item);
              setCurrentIndex(index);
            }}
          >
            {item.type === 'image' ? (
              <img
                src={item.thumbnailUrl || item.url}
                alt={item.caption || 'Review media'}
                className="w-full h-full object-cover"
              />
            ) : (
              <video
                src={item.url}
                poster={item.thumbnailUrl}
                className="w-full h-full object-cover"
              />
            )}
            <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-30 transition-opacity flex items-center justify-center">
              <FiMaximize2 className="text-white opacity-0 group-hover:opacity-100 w-6 h-6" />
            </div>
            {item.caption && (
              <div className="absolute bottom-0 left-0 right-0 p-2 bg-gradient-to-t from-black/70 to-transparent">
                <p className="text-white text-sm truncate">{item.caption}</p>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Lightbox */}
      <AnimatePresence>
        {selectedMedia && (
          <div
            className="fixed inset-0 bg-black/90 z-50 flex items-center justify-center"
            onClick={() => setSelectedMedia(null)}
          >
            <motion.div
              initial={{ opacity: 0, scale: 0.9 }}
              animate={{ opacity: 1, scale: 1 }}
              exit={{ opacity: 0, scale: 0.9 }}
              className="relative max-w-4xl w-full mx-4"
              onClick={(e) => e.stopPropagation()}
            >
              <button
                onClick={() => setSelectedMedia(null)}
                className="absolute top-4 right-4 text-white hover:text-gray-300 z-10"
              >
                <FiX className="w-6 h-6" />
              </button>

              <div className="relative">
                {selectedMedia.type === 'image' ? (
                  <img
                    src={selectedMedia.url}
                    alt={selectedMedia.caption || 'Review media'}
                    className="w-full rounded-lg"
                  />
                ) : (
                  <video
                    src={selectedMedia.url}
                    poster={selectedMedia.thumbnailUrl}
                    controls
                    className="w-full rounded-lg"
                  />
                )}

                {currentIndex > 0 && (
                  <button
                    onClick={handlePrevious}
                    className="absolute left-4 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full"
                  >
                    <FiChevronLeft className="w-6 h-6" />
                  </button>
                )}

                {currentIndex < media.length - 1 && (
                  <button
                    onClick={handleNext}
                    className="absolute right-4 top-1/2 -translate-y-1/2 bg-black/50 hover:bg-black/70 text-white p-2 rounded-full"
                  >
                    <FiChevronRight className="w-6 h-6" />
                  </button>
                )}

                {selectedMedia.caption && (
                  <div className="absolute bottom-0 left-0 right-0 p-4 bg-gradient-to-t from-black/70 to-transparent">
                    <p className="text-white text-center">{selectedMedia.caption}</p>
                  </div>
                )}
              </div>
            </motion.div>
          </div>
        )}
      </AnimatePresence>
    </div>
  );
};

export default ReviewMediaGallery; 
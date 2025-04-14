import { motion } from "framer-motion";
import { FiVolume2, FiStar, FiEdit2, FiTrash2 } from "react-icons/fi";
import { VocabularyWord } from "../../../management/personal/components/PersonalVocabularyCard";
import { levelColors } from "../../../management/personal/components/PersonalVocabularyCard";
import { HiMiniSpeakerWave } from "react-icons/hi2";

interface VocabularyDetailsModalProps {
  word: VocabularyWord;
  onClose: () => void;
  onFavoriteToggle: (id: string) => void;
  onPlayPronunciation: (word: string) => void;
}

const VocabularyDetailsModal: React.FC<VocabularyDetailsModalProps> = ({
  word,
  onClose,
  onFavoriteToggle,
  onPlayPronunciation,
}) => {
  return (
    <div className="fixed inset-0 bg-black bg-opacity-75 flex items-center justify-center p-4 z-50 backdrop-blur-sm">
      <motion.div
        className="bg-[#0F172A] rounded-xl shadow-2xl max-w-3xl w-full max-h-[90vh] overflow-y-auto"
        initial={{ opacity: 0, scale: 0.9 }}
        animate={{ opacity: 1, scale: 1 }}
        exit={{ opacity: 0, scale: 0.9 }}
      >
        {/* Header Section */}
        <div className="bg-gradient-to-br from-blue-900 to-[#1E293B] p-6 rounded-t-xl border-b border-blue-800/30">
          <div className="flex justify-between items-start mb-4">
            <div>
              <div className="flex items-center gap-3 mb-2">
                <h2 className="text-3xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-blue-400 to-blue-300">
                  {word.word}
                </h2>
                <span className="text-blue-300 text-sm px-3 py-1 bg-blue-950/50 rounded-full border border-blue-800/30">
                  {word.tags[0] || "unknown"}
                </span>
              </div>
              <div className="space-y-2">
                <div className="flex items-center gap-3">
                  <span className="text-blue-300 text-sm font-medium bg-blue-950/50 px-2 py-0.5 rounded border border-blue-800/30">
                    UK/US
                  </span>
                  <button 
                    className="text-blue-400 hover:text-blue-300 transition-colors"
                    onClick={() => onPlayPronunciation(word.word)}
                  >
                    <HiMiniSpeakerWave className="w-5 h-5" />
                  </button>
                  <span className="text-blue-200 font-medium">{word.pronunciation}</span>
                </div>
              </div>
            </div>
            <div className="flex space-x-3">
              <button
                className={`${
                  word.isFavorite
                    ? "text-yellow-400"
                    : "text-gray-400"
                } hover:text-yellow-300 transition-colors`}
                onClick={() => onFavoriteToggle(word.id)}
              >
                <FiStar size={24} />
              </button>
              <button className="text-gray-400 hover:text-blue-400 transition-colors">
                <FiEdit2 size={24} />
              </button>
              <button className="text-gray-400 hover:text-red-400 transition-colors">
                <FiTrash2 size={24} />
              </button>
            </div>
          </div>

          <div className="flex items-center gap-2 mt-4">
            <span
              className={`px-3 py-1 rounded-full text-sm font-medium border border-blue-800/30 ${
                levelColors[word.level].dark
              } dark:${levelColors[word.level].dark} ${
                levelColors[word.level].light
              } transition-colors`}
            >
              {word.level.replace("_", " ")}
            </span>
            <span className="text-blue-300/70 text-sm">
              Last reviewed: {new Date(word.lastReviewed).toLocaleDateString()}
            </span>
          </div>
        </div>

        {/* Content Section */}
        <div className="p-6 space-y-6 bg-[#0F172A]">
          {/* Meaning Section */}
          <div className="bg-[#1E293B] p-4 rounded-lg border border-blue-800/20">
            <h3 className="text-lg font-semibold text-blue-300 mb-3">
              Meaning
            </h3>
            <p className="text-gray-200 text-lg leading-relaxed">
              {word.meaning}
            </p>
          </div>

          {/* Example Section */}
          <div className="bg-[#1E293B] p-4 rounded-lg border border-blue-800/20">
            <h3 className="text-lg font-semibold text-blue-300 mb-3">
              Example
            </h3>
            <p className="text-gray-200 italic text-lg">
              "{word.example}"
            </p>
          </div>

          {/* Tags Section */}
          <div className="bg-[#1E293B] p-4 rounded-lg border border-blue-800/20">
            <h3 className="text-lg font-semibold text-blue-300 mb-3">
              Tags
            </h3>
            <div className="flex flex-wrap gap-2">
              {word.tags.map((tag) => (
                <span
                  key={tag}
                  className="bg-blue-950/50 text-blue-200 px-3 py-1.5 rounded-full text-sm border border-blue-800/30"
                >
                  {tag}
                </span>
              ))}
            </div>
          </div>

          {/* Mastery Progress Section */}
          <div className="bg-[#1E293B] p-4 rounded-lg border border-blue-800/20">
            <h3 className="text-lg font-semibold text-blue-300 mb-3">
              Mastery Progress
            </h3>
            <div className="h-3 bg-blue-950 rounded-full mb-3">
              <div
                className="h-3 bg-gradient-to-r from-blue-500 to-blue-400 rounded-full"
                style={{ width: `${word.mastery}%` }}
              ></div>
            </div>
            <div className="flex justify-between text-sm">
              <span className="text-blue-300/70">Beginner</span>
              <span className="text-blue-400 font-medium">{word.mastery}%</span>
              <span className="text-blue-300/70">Mastered</span>
            </div>
          </div>
        </div>

        {/* Footer Section */}
        <div className="border-t border-blue-800/30 p-6 flex justify-between items-center bg-[#1E293B]">
          <button
            className="text-blue-300/70 hover:text-blue-200 font-medium transition-colors"
            onClick={onClose}
          >
            Close
          </button>
          <button className="bg-gradient-to-r from-blue-600 to-blue-500 hover:from-blue-700 hover:to-blue-600 text-white px-6 py-2 rounded-lg font-medium transition-all duration-300 transform hover:scale-105 shadow-lg">
            Practice Now
          </button>
        </div>
      </motion.div>
    </div>
  );
};

export default VocabularyDetailsModal; 
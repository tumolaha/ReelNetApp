import { motion } from "framer-motion";
import { useState } from "react";
import { HiOutlineVolumeUp } from "react-icons/hi";
import { IoCheckmark, IoRefresh, IoShuffle } from "react-icons/io5";
import { IoClose } from "react-icons/io5";

interface FlashCardProps {
  frontContent: {
    question?: string;
    options?: string[];
    translation?: string;
  };
  backContent: {
    answer: string;
    translation: string;
  };
}

const ContentFlashCardWord = () => {
  const [isFlipped, setIsFlipped] = useState(false);
  const [showHint, setShowHint] = useState(false);

  const sampleCard: FlashCardProps = {
    frontContent: {
      question:
        "All of the trees on the property were damaged in the storm ___ the one near the rear entrance.",
    },
    backContent: {
      answer: "A. except",
      translation:
        "Tất cả cây cối trong khuôn viên đã bị hư hại trong trận bão trừ cái cây ở cổng vào phía sau.",
    },
  };

  return (
    <div className="w-full h-[calc(100vh-60px)] max-w-[1200px] mx-auto p-6 ">
      <div className="flex items-start justify-between h-[60px]">
        <div className="flex items-center gap-2">
          <span className="w-[50px] h-[30px] flex items-center justify-center bg-orange-500/10 border border-orange-500/30 text-red-500 px-3 py-1 rounded-full text-sm">
            0
          </span>
          <span className="text-red-500 dark:text-red-400 text-sm font-medium">
            Đang học
          </span>
        </div>
        <div className="flex items-center gap-2">
          <span className="w-[50px] h-[30px] flex items-center justify-center bg-green-500/10 border border-green-500/30 text-green-500 px-3 py-1 rounded-full text-sm">
            0
          </span>
          <span className="text-green-500 dark:text-green-400 text-sm font-medium">
            Đã biết
          </span>
        </div>
      </div>

      {/* Card Container với perspective */}
      <div
        className="relative w-full  h-[calc(100%-120px)] aspect-[3/2] cursor-pointer perspective-1000"
        onClick={() => setIsFlipped(!isFlipped)}
      >
        <div className="relative preserve-3d w-full h-full">
          {/* Front Card */}
          <motion.div
            className="absolute w-full h-full backface-hidden"
            animate={{
              rotateX: isFlipped ? 180 : 0,
              scale: isFlipped ? 0.95 : 1,
              opacity: isFlipped ? 0 : 1,
            }}
            transition={{
              duration: 0.4,
              ease: "easeOut",
              opacity: { duration: 0.2 },
            }}
          >
            <div className="w-full h-full dark:bg-gray-800 bg-gray-200 rounded-xl p-8 shadow-2xl">
              <div className="flex justify-between items-start mb-4">
                <button className="text-gray-400 hover:text-gray-300" onClick={(e) => {
                  e.stopPropagation();
                  console.log("click");
                }}>
                  <HiOutlineVolumeUp className="w-6 h-6" />
                </button>
                <button
                  className="text-gray-400 hover:text-gray-300"
                  onClick={(e) => {
                    e.stopPropagation();
                    setShowHint(!showHint);
                  }}
                >
                  <svg
                    className="w-6 h-6"
                    fill="none"
                    stroke="currentColor"
                    viewBox="0 0 24 24"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
                    />
                  </svg>
                </button>
              </div>

              <div className="flex-1 flex flex-col items-center justify-center text-gray-200">
                <p className="text-xl text-center mb-6">
                  {sampleCard.frontContent.question}
                </p>

                {showHint && (
                  <p className="mt-6 text-sm text-gray-400 text-center">
                    {sampleCard.frontContent.translation}
                  </p>
                )}
              </div>
            </div>
          </motion.div>

          {/* Back Card */}
          <motion.div
            className="absolute w-full h-full backface-hidden"
            animate={{
              rotateX: isFlipped ? 0 : -180,
              scale: isFlipped ? 1 : 0.95,
              opacity: isFlipped ? 1 : 0,
            }}
            transition={{
              duration: 0.4,
              ease: "easeOut",
              opacity: { duration: 0.2 },
            }}
          >
            <div className="w-full h-full bg-gray-200 dark:bg-gray-800 rounded-xl p-8 shadow-2xl">
              <div className="flex-1 flex flex-col justify-center items-center text-gray-200">
                <div className="text-2xl font-medium mb-4 text-green-500">
                  {sampleCard.backContent.answer}
                </div>
                <p className="text-gray-400 text-center">
                  {sampleCard.backContent.translation}
                </p>
              </div>
            </div>
          </motion.div>
        </div>
      </div>

      <div className="flex items-center justify-between gap-4 h-[60px]">
        <div className="flex items-center gap-2">
          <span className="text-gray-400 text-sm">Theo dõi tiến độ</span>
          <div className="relative inline-flex items-center">
            <input
              type="checkbox"
              className="sr-only peer"
              checked={true}
              onChange={() => {}}
            />
            <div className="w-11 h-6 bg-gray-700 peer-focus:outline-none rounded-full peer peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600"></div>
          </div>
        </div>
        <div className="flex items-center gap-4">
          <button className="p-3 rounded-full bg-gray-800 text-red-500 hover:bg-gray-700">
            <IoClose className="w-6 h-6" />
          </button>
          <button className="p-3 rounded-full bg-gray-800 text-green-500 hover:bg-gray-700">
            <IoCheckmark className="w-6 h-6" />
          </button>
        </div>

        <div className="flex items-center gap-4">
          <button className="p-3 rounded-full bg-gray-800 text-gray-400 hover:bg-gray-700">
            <IoRefresh className="w-6 h-6" />
          </button>
          <button className="p-3 rounded-full bg-gray-800 text-gray-400 hover:bg-gray-700">
            <IoShuffle className="w-6 h-6" />
          </button>
        </div>
      </div>
    </div>
  );
};

// Thêm CSS global cho animation


export default ContentFlashCardWord;

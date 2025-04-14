import { motion, useSpring } from "framer-motion";
import { useScroll } from "framer-motion";
import { useEffect, useRef, useState } from "react";

const SwiperScroll = ({ children }: { children: React.ReactNode }) => {
  const scrollRef = useRef<HTMLDivElement>(null);
  const [isScrollable, setIsScrollable] = useState<boolean>(false);
  const [showPrev, setShowPrev] = useState<boolean>(false);
  const [showNext, setShowNext] = useState<boolean>(false);
  const { scrollXProgress } = useScroll({ container: scrollRef });
  const scaleX = useSpring(scrollXProgress, {
    stiffness: 100,
    damping: 30,
    restDelta: 0.001,
  });

  const checkScrollButtons = () => {
    if (scrollRef.current) {
      const { scrollLeft, scrollWidth, clientWidth } = scrollRef.current;
      setShowPrev(scrollLeft > 0);
      setShowNext(scrollLeft + clientWidth < scrollWidth);
    }
  };

  // Check if content is scrollable
  useEffect(() => {
    if (scrollRef.current) {
      const { scrollWidth, clientWidth } = scrollRef.current;
      setIsScrollable(scrollWidth > clientWidth);
      setShowNext(scrollWidth > clientWidth);
    }
  }, []);

  useEffect(() => {
    checkScrollButtons();
    // Add scroll event listener
    const currentRef = scrollRef.current;
    currentRef?.addEventListener("scroll", checkScrollButtons);
    return () => {
      currentRef?.removeEventListener("scroll", checkScrollButtons);
    };
  }, []);

  const handleScrollLeft = () => {
    scrollRef.current?.scrollBy({ left: -300, behavior: "smooth" });
  };

  const handleScrollRight = () => {
    scrollRef.current?.scrollBy({ left: 300, behavior: "smooth" });
  };

  return (
    <div className="relative mb-5">
      <div
        ref={scrollRef}
        className="flex gap-4 overflow-x-auto scrollbar-hide pb-4 -mx-2 px-2"
        style={{
          scrollbarWidth: "none",
          msOverflowStyle: "none",
        }}
      >
        {children}
      </div>
      {isScrollable && (
        <>
          {showPrev && (
            <button
              onClick={handleScrollLeft}
              className="p-2 rounded-full bg-gray-100 hover:bg-gray-200 dark:bg-gray-800 dark:hover:bg-gray-700 transition-colors absolute left-4"
            >
              <svg
                className="w-5 h-5 text-gray-600 dark:text-gray-400"
                viewBox="0 0 24 24"
              >
                <path
                  fill="currentColor"
                  d="M15.41 7.41L14 6l-6 6 6 6 1.41-1.41L10.83 12z"
                />
              </svg>
            </button>
          )}
          {showNext && (
            <button
              onClick={handleScrollRight}
              className="p-2 rounded-full bg-gray-100 hover:bg-gray-200 dark:bg-gray-800 dark:hover:bg-gray-700 transition-colors absolute right-4"
            >
              <svg
                className="w-5 h-5 text-gray-600 dark:text-gray-400"
                viewBox="0 0 24 24"
              >
                <path
                  fill="currentColor"
                  d="M10 6L8.59 7.41 13.17 12l-4.58 4.59L10 18l6-6z"
                />
              </svg>
            </button>
          )}
        </>
      )}

      {isScrollable && (
        <div className="mb-6 mt-4 mx-auto max-w-[50%]">
          <div className="h-1 bg-gray-200 dark:bg-gray-800 rounded-full">
            <motion.div
              className="h-full bg-blue-500 rounded-full"
              style={{ scaleX }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default SwiperScroll;

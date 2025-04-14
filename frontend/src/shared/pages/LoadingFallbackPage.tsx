import { motion } from 'framer-motion';

const LoadingFallbackPage = () => {
  return (
    <motion.div 
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      className="min-h-screen bg-white dark:bg-gray-900 flex items-center justify-center p-4 relative overflow-hidden"
    >
      {/* Background animated shapes */}
      <motion.div 
        animate={{
          scale: [1, 1.3, 1],
          rotate: [0, 180, 360],
        }}
        transition={{
          duration: 20,
          repeat: Infinity,
          ease: "linear"
        }}
        className="absolute w-96 h-96 bg-blue-200/50 dark:bg-blue-400/30 rounded-full filter blur-3xl animate-blob"
      />
      <motion.div 
        animate={{
          scale: [1, 1.2, 1],
          rotate: [360, 180, 0],
        }}
        transition={{
          duration: 15,
          repeat: Infinity,
          ease: "linear"
        }}
        className="absolute w-96 h-96 bg-purple-200/50 dark:bg-purple-400/30 rounded-full filter blur-3xl animate-blob" 
        style={{top: '60%', left: '60%'}}
      />
      
      <motion.div 
        initial={{ scale: 0.8, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        transition={{ duration: 0.5 }}
        className="text-center relative z-10 backdrop-blur-sm bg-white/90 dark:bg-gray-800/90 p-12 rounded-2xl shadow-2xl"
      >
        <motion.div 
          className="relative"
          whileHover={{ scale: 1.05 }}
          transition={{ type: "spring", stiffness: 300 }}
        >
          {/* Main spinner */}
          <div className="w-36 h-36 border-8 border-blue-200 dark:border-blue-300/30 border-t-blue-600 dark:border-t-blue-400 rounded-full animate-spin mx-auto"></div>
          {/* Inner icon with subtle effect */}
          <motion.div 
            animate={{
              scale: [1, 1.1, 1],
            }}
            transition={{
              duration: 2,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            className="absolute top-[calc(50%-40px)] left-[calc(50%-40px)] flex items-center justify-center"
          >
            <div className="relative w-20 h-20">
              <motion.div 
                animate={{
                  opacity: [0.5, 1, 0.5],
                }}
                transition={{
                  duration: 2,
                  repeat: Infinity,
                  ease: "easeInOut"
                }}
                className="absolute inset-0 blur-md bg-blue-500/30 dark:bg-blue-400/50 rounded-full"
              />
              <svg className="w-full h-full text-blue-600 dark:text-blue-400 animate-pulse relative z-10" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 10l4.553-2.276A1 1 0 0121 8.618v6.764a1 1 0 01-1.447.894L15 14M5 18h8a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v8a2 2 0 002 2z"></path>
              </svg>
            </div>
          </motion.div>
        </motion.div>
        
        <motion.h2 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.2 }}
          className="mt-10 text-4xl font-bold"
        >
          <span className="bg-clip-text text-transparent bg-gradient-to-r from-blue-600 to-purple-600 dark:from-blue-400 dark:to-purple-400">
            ReelNet
          </span>
        </motion.h2>
        
        <motion.p
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.4 }}
          className="mt-6 text-gray-700 dark:text-gray-300 max-w-md mx-auto text-lg font-light tracking-wide"
        >
          Đang tải không gian sáng tạo của bạn...
        </motion.p>
        
        {/* Enhanced loading dots */}
        <motion.div 
          initial={{ y: 20, opacity: 0 }}
          animate={{ y: 0, opacity: 1 }}
          transition={{ delay: 0.6 }}
          className="mt-8 flex justify-center space-x-4"
        >
          <motion.div
            animate={{
              y: [-10, 0, -10],
              scale: [1, 1.2, 1]
            }}
            transition={{
              duration: 1,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            className="w-4 h-4 bg-gradient-to-r from-blue-400 to-purple-500 dark:from-blue-300 dark:to-purple-400 rounded-full shadow-lg shadow-blue-400/50"
          />
          <motion.div
            animate={{
              y: [-10, 0, -10],
              scale: [1, 1.2, 1]
            }}
            transition={{
              duration: 1,
              delay: 0.2,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            className="w-4 h-4 bg-gradient-to-r from-blue-400 to-purple-500 dark:from-blue-300 dark:to-purple-400 rounded-full shadow-lg shadow-blue-400/50"
          />
          <motion.div
            animate={{
              y: [-10, 0, -10],
              scale: [1, 1.2, 1]
            }}
            transition={{
              duration: 1,
              delay: 0.4,
              repeat: Infinity,
              ease: "easeInOut"
            }}
            className="w-4 h-4 bg-gradient-to-r from-blue-400 to-purple-500 dark:from-blue-300 dark:to-purple-400 rounded-full shadow-lg shadow-blue-400/50"
          />
        </motion.div>
      </motion.div>
    </motion.div>
  );
};

export default LoadingFallbackPage;
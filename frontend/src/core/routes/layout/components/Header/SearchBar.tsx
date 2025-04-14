
const SearchBar = () => {
  return (
    <div className="flex items-center relative">
      <input
        type="text"
        placeholder="Tìm kiếm..."
        className="w-96 pl-10 pr-4 py-2 text-sm text-gray-700 dark:text-gray-200 bg-gray-100 dark:bg-gray-800 
                   border border-gray-200 dark:border-gray-700 rounded-xl
                   focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent
                   placeholder-gray-400 dark:placeholder-gray-500
                   transition-all duration-200"
      />
      <svg 
        className="w-5 h-5 absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-gray-500"
        fill="none" 
        stroke="currentColor" 
        viewBox="0 0 24 24"
      >
        <path 
          strokeLinecap="round" 
          strokeLinejoin="round" 
          strokeWidth={2} 
          d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" 
        />
      </svg>
    </div>
  );
};

export default SearchBar
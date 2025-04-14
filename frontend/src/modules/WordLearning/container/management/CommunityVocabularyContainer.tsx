import { useNavigate } from "react-router-dom";
import { useState, useMemo, useEffect } from "react";
import {
  Search,
  BookOpen,
  Plus,
  Share,
  Filter,
  ThumbsUp,
  Loader2,
  X,
} from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";
import {
  Card,
  CardHeader,
  CardTitle,
  CardDescription,
  CardContent,
  CardFooter,
} from "@/shared/components/ui/card";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "@/shared/components/ui/tabs";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/shared/components/ui/table";
import { Progress } from "@/shared/components/ui/progress";
import PackageVocabulariesCard from "../../features/management/community/components/PackageVocabulariesCard";
import PackageVocabulariesRecentCard from "../../features/management/community/components/PackageVocabulariesRecentCard";
import { ROUTES } from "@/core/routes/constants";
import { useGetAllVocabularySetsQuery } from "../../api/vocabularySetApi";
import { useDebounce } from "@/shared/hooks/useDebounce";
import { VocabularyQueryParams } from "../../api/vocabularySetApi";
import { Badge } from "@/shared/components/ui/badge";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/shared/components/ui/dropdown-menu";
import { useToast } from "@/shared/hooks/use-toast";
import { CATEGORY_LIST } from "../../constants/category.constants";

// Định nghĩa kiểu dữ liệu cho các tham số
type SortField = "createdAt" | "name" | "itemCount" | "likeCount";
type SortDirection = "asc" | "desc";
type TabValue = "featured" | "recent" | "popular";

const CommunityVocabularyContainer = () => {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [searchTerm, setSearchTerm] = useState("");
  const [selectedCategory, setSelectedCategory] = useState<string | null>(null);
  const [sortBy, setSortBy] = useState<SortField>("createdAt");
  const [sortDirection, setSortDirection] = useState<SortDirection>("desc");
  const [activeTab, setActiveTab] = useState<TabValue>("featured");
  
  // Debounce search term to avoid excessive API calls
  const debouncedSearchTerm = useDebounce(searchTerm, 500);
  
  // Create query parameters object using useMemo to prevent unnecessary re-renders
  const queryParams: VocabularyQueryParams = useMemo(() => {
    const params: VocabularyQueryParams = {
      page: 0,
      size: 10,
      sortBy,
      sortDirection,
      useCache: true,
      includeDeleted: false,
    };
    
    // Add search parameters if search term exists
    if (debouncedSearchTerm) {
      params.searchText = debouncedSearchTerm;
      params.searchFields = ["name", "description", "tags"];
    }
    
    // Add category filter if selected
    if (selectedCategory) {
      params.category = selectedCategory;
    }
    
    // Add specific filters based on active tab
    if (activeTab === "featured") {
      params.isFeatured = true;
    } else if (activeTab === "recent") {
      // Sort by creation date for recent items
      params.sortBy = "createdAt";
      params.sortDirection = "desc";
    } else if (activeTab === "popular") {
      // Sort by like count for popular items
      params.sortBy = "likeCount";
      params.sortDirection = "desc";
    }
    
    return params;
  }, [debouncedSearchTerm, selectedCategory, sortBy, sortDirection, activeTab]);

  // Fetch vocabulary sets with optimized query parameters
  const {
    data: featuredVocabPackages,
    isLoading: isLoadingFeaturedVocabPackages,
    error: featuredError,
  } = useGetAllVocabularySetsQuery(queryParams, {
    // Sử dụng pollingInterval thay vì refetchOnMountOrArgChange
    pollingInterval: 0,
  });

  // Hiển thị toast khi có lỗi
  useEffect(() => {
    if (featuredError) {
      toast({
        title: "Error Loading Vocabulary Sets",
        description: "There was a problem loading the vocabulary sets. Please try again later.",
        variant: "destructive",
        showIcon: true,
      });
    }
  }, [featuredError, toast]);

  // Handle search input change
  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };
  
  // Clear search
  const clearSearch = () => {
    setSearchTerm("");
  };
  
  // Handle category selection
  const handleCategorySelect = (category: string | null) => {
    setSelectedCategory(category);
  };
  
  // Handle sort change
  const handleSortChange = (newSortBy: SortField, newDirection: SortDirection) => {
    setSortBy(newSortBy);
    setSortDirection(newDirection);
  };
  
  // Handle tab change
  const handleTabChange = (value: string) => {
    setActiveTab(value as TabValue);
  };



  const topContributors = [
    { name: "Sarah Johnson", sets: 28, followers: 1203 },
    { name: "Michael Chen", sets: 23, followers: 987 },
    { name: "Emma Thompson", sets: 17, followers: 756 },
    { name: "David Wilson", sets: 15, followers: 624 },
  ];

  return (
    <div className="container py-8 max-w-7xl mx-auto">
      {/* Header with search and filter */}
      <div className="flex flex-col md:flex-row justify-between items-center mb-8 gap-4">
        <div>
          <h1 className="text-3xl font-bold mb-2">
            Community Vocabulary Lists
          </h1>
          <p className="text-muted-foreground">
            Discover, share, and learn from vocabulary lists created by the
            community
          </p>
        </div>
        <div className="flex gap-3 w-full md:w-auto">
          <div className="relative flex-1 md:w-64">
            <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
            <Input
              type="search"
              placeholder="Search vocabulary lists..."
              className="pl-8 w-full"
              value={searchTerm}
              onChange={handleSearchChange}
            />
            {searchTerm && (
              <Button
                variant="ghost"
                size="icon"
                className="absolute right-2 top-1/2 -translate-y-1/2 h-6 w-6"
                onClick={clearSearch}
              >
                <X className="h-4 w-4" />
              </Button>
            )}
          </div>
          
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="outline" size="icon">
                <Filter className="h-4 w-4" />
              </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-56">
              <div className="p-2">
                <h4 className="mb-2 text-sm font-medium">Categories</h4>
                <div className="flex flex-wrap gap-1">
                  <Badge 
                    variant={selectedCategory === null ? "default" : "outline"} 
                    className="cursor-pointer"
                    onClick={() => handleCategorySelect(null)}
                  >
                    All
                  </Badge>
                  {CATEGORY_LIST.slice(0, 5).map((category) => (
                    <Badge 
                      key={category.value}
                      variant={selectedCategory === category.value ? "default" : "outline"} 
                      className="cursor-pointer"
                      onClick={() => handleCategorySelect(category.value)}
                    >
                      {category.label}
                    </Badge>
                  ))}
                </div>
              </div>
              <div className="p-2 border-t">
                <h4 className="mb-2 text-sm font-medium">Sort By</h4>
                <div className="space-y-1">
                  <DropdownMenuItem 
                    className="cursor-pointer"
                    onClick={() => handleSortChange("createdAt", sortDirection === "asc" ? "desc" : "asc")}
                  >
                    Date {sortBy === "createdAt" && (sortDirection === "asc" ? "↑" : "↓")}
                  </DropdownMenuItem>
                  <DropdownMenuItem 
                    className="cursor-pointer"
                    onClick={() => handleSortChange("name", sortDirection === "asc" ? "desc" : "asc")}
                  >
                    Name {sortBy === "name" && (sortDirection === "asc" ? "↑" : "↓")}
                  </DropdownMenuItem>
                  <DropdownMenuItem 
                    className="cursor-pointer"
                    onClick={() => handleSortChange("itemCount", sortDirection === "asc" ? "desc" : "asc")}
                  >
                    Word Count {sortBy === "itemCount" && (sortDirection === "asc" ? "↑" : "↓")}
                  </DropdownMenuItem>
                  <DropdownMenuItem 
                    className="cursor-pointer"
                    onClick={() => handleSortChange("likeCount", sortDirection === "asc" ? "desc" : "asc")}
                  >
                    Popularity {sortBy === "likeCount" && (sortDirection === "asc" ? "↑" : "↓")}
                  </DropdownMenuItem>
                </div>
              </div>
            </DropdownMenuContent>
          </DropdownMenu>
          
          <Button
            onClick={() =>
              navigate(ROUTES.VOCABULARIES.CREATE_PACKAGE_VOCABULARIES)
            }
          >
            <Plus className="mr-2 h-4 w-4" /> Create List
          </Button>
        </div>
      </div>

      {/* Active filters display */}
      {(selectedCategory || searchTerm) && (
        <div className="mb-4 flex flex-wrap gap-2 items-center">
          <span className="text-sm text-muted-foreground">Filters:</span>
          {selectedCategory && (
            <Badge variant="secondary" className="flex items-center gap-1">
              {selectedCategory}
              <X 
                className="h-3 w-3 cursor-pointer" 
                onClick={() => handleCategorySelect(null)}
              />
            </Badge>
          )}
          {searchTerm && (
            <Badge variant="secondary" className="flex items-center gap-1">
              Search: {searchTerm}
              <X 
                className="h-3 w-3 cursor-pointer" 
                onClick={clearSearch}
              />
            </Badge>
          )}
        </div>
      )}

      {/* Main content */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Vocabulary lists section */}
        <div className="lg:col-span-2">
          <Tabs defaultValue="featured" className="w-full" onValueChange={handleTabChange}>
            <TabsList className="mb-6 grid w-full grid-cols-3">
              <TabsTrigger value="featured">Featured</TabsTrigger>
              <TabsTrigger value="recent">Recent</TabsTrigger>
              <TabsTrigger value="popular">Popular</TabsTrigger>
            </TabsList>

            <TabsContent value="featured" className="space-y-6">
              {isLoadingFeaturedVocabPackages ? (
                <div className="flex justify-center items-center h-full">
                  <Loader2 className="h-4 w-4 animate-spin" />
                </div>
              ) : featuredVocabPackages?.data?.content?.length ? (
                featuredVocabPackages.data.content.map((set) => (
                  <PackageVocabulariesCard key={set.id} set={set} />
                ))
              ) : (
                <div className="flex flex-col items-center justify-center py-12 text-center">
                  <BookOpen className="h-12 w-12 text-muted-foreground mb-4" />
                  <h3 className="text-lg font-medium mb-2">
                    No vocabulary sets found
                  </h3>
                  <p className="text-muted-foreground mb-4">
                    {searchTerm || selectedCategory 
                      ? "Try adjusting your search or filters" 
                      : "There are currently no featured vocabulary sets available."}
                  </p>
                  <Button
                    onClick={() =>
                      navigate(ROUTES.VOCABULARIES.CREATE_PACKAGE_VOCABULARIES)
                    }
                  >
                    <Plus className="mr-2 h-4 w-4" /> Create a vocabulary list
                  </Button>
                </div>
              )}
            </TabsContent>

            <TabsContent value="recent" className="space-y-4">
              <div className="flex justify-between items-center mb-2">
                <h3 className="text-lg font-medium">Recently Added</h3>
              </div>

              {isLoadingFeaturedVocabPackages ? (
                <div className="flex justify-center items-center h-full">
                  <Loader2 className="h-4 w-4 animate-spin" />
                </div>
              ) : featuredVocabPackages?.data?.content?.length ? (
                featuredVocabPackages.data.content?.map((set) => (
                  <PackageVocabulariesRecentCard key={set.id} set={set} />
                ))
              ) : (
                <div className="flex flex-col items-center justify-center py-12 text-center">
                  <BookOpen className="h-12 w-12 text-muted-foreground mb-4" />
                  <h3 className="text-lg font-medium mb-2">
                    No recent vocabulary lists
                  </h3>
                  <p className="text-muted-foreground mb-4">
                    {searchTerm || selectedCategory 
                      ? "Try adjusting your search or filters" 
                      : "There are currently no recent vocabulary lists available."}
                  </p>
                  <Button
                    onClick={() =>
                      navigate(ROUTES.VOCABULARIES.CREATE_PACKAGE_VOCABULARIES)
                    }
                  >
                    <Plus className="mr-2 h-4 w-4" /> Create a vocabulary list
                  </Button>
                </div>
              )}
            </TabsContent>

            <TabsContent value="popular" className="space-y-4">
              <div className="bg-muted p-4 rounded-lg text-center">
                <h3 className="font-medium mb-2">
                  Most Downloaded Vocabulary Lists
                </h3>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Title</TableHead>
                      <TableHead>Category</TableHead>
                      <TableHead>Words</TableHead>
                      <TableHead>Downloads</TableHead>
                      <TableHead className="text-right">Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {isLoadingFeaturedVocabPackages ? (
                      <TableRow>
                        <TableCell colSpan={5} className="text-center">
                          <Loader2 className="h-4 w-4 animate-spin" />
                        </TableCell>
                      </TableRow>
                    ) : featuredVocabPackages?.data?.content?.length ? (
                      featuredVocabPackages.data.content.map((set) => (
                        <TableRow key={set.id}>
                          <TableCell className="font-medium">
                            {set.name}
                          </TableCell>{" "}
                          <TableCell>
                            {set.category || "General"}
                          </TableCell>
                          <TableCell>{set.itemCount}</TableCell>
                          <TableCell>{set.likeCount}</TableCell>
                          <TableCell className="text-right">
                            <Button
                              variant="ghost"
                              size="sm"
                              onClick={() =>
                                navigate(
                                  ROUTES.VOCABULARIES.OVERVIEW_DETAIL_PACKAGE_VOCABULARY.replace(
                                    ":id",
                                    set.id
                                  )
                                )
                              }
                            >
                              View
                            </Button>
                          </TableCell>
                        </TableRow>
                      ))
                    ) : (
                      <TableRow>
                        <TableCell colSpan={5} className="h-32">
                          <div className="flex flex-col items-center justify-center text-center">
                            <BookOpen className="h-8 w-8 text-muted-foreground mb-2" />
                            <h3 className="text-base font-medium">
                              No popular vocabulary lists
                            </h3>
                            <p className="text-sm text-muted-foreground mb-3">
                              {searchTerm || selectedCategory 
                                ? "Try adjusting your search or filters" 
                                : "There are currently no popular vocabulary lists available."}
                            </p>
                            <Button
                              size="sm"
                              onClick={() =>
                                navigate(
                                  ROUTES.VOCABULARIES
                                    .CREATE_PACKAGE_VOCABULARIES
                                )
                              }
                            >
                              <Plus className="mr-2 h-4 w-4" /> Create a list
                            </Button>
                          </div>
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </div>
            </TabsContent>
          </Tabs>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* My progress */}
          <Card>
            <CardHeader>
              <CardTitle className="text-lg">My Learning Progress</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <div className="flex justify-between mb-1 text-sm">
                  <span>TOEFL Essential Vocabulary</span>
                  <span className="text-muted-foreground">35%</span>
                </div>
                <Progress value={35} className="h-2" />
              </div>
              <div>
                <div className="flex justify-between mb-1 text-sm">
                  <span>Business English Terms</span>
                  <span className="text-muted-foreground">68%</span>
                </div>
                <Progress value={68} className="h-2" />
              </div>
              <div>
                <div className="flex justify-between mb-1 text-sm">
                  <span>Daily Conversation Phrases</span>
                  <span className="text-muted-foreground">92%</span>
                </div>
                <Progress value={92} className="h-2" />
              </div>
            </CardContent>
            <CardFooter>
              <Button variant="outline" className="w-full">
                View All My Lists
              </Button>
            </CardFooter>
          </Card>

          {/* Popular tags */}
          <Card>
            <CardHeader>
              <CardTitle className="text-lg">Popular Tags</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex flex-wrap gap-2">
                {CATEGORY_LIST?.map((category, index) => (
                  <Badge
                    key={index}
                    variant={selectedCategory === category.value ? "default" : "outline"}
                    className="cursor-pointer"
                    onClick={() => handleCategorySelect(category.value)}
                  >
                    {category.label}
                  </Badge>
                ))}
              </div>
            </CardContent>
          </Card>

          {/* Top contributors */}
          <Card>
            <CardHeader>
              <CardTitle className="text-lg">Top Contributors</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              {topContributors.map((contributor, index) => (
                <div key={index} className="flex items-center justify-between">
                  <div className="flex items-center">
                    <div className="w-8 h-8 rounded-full bg-blue-500 text-white flex items-center justify-center mr-3 uppercase font-bold">
                      {contributor.name.charAt(0)}
                    </div>
                    <div>
                      <div className="font-medium">{contributor.name}</div>
                      <div className="text-xs text-muted-foreground">
                        {contributor.sets} vocabulary sets
                      </div>
                    </div>
                  </div>
                  <Button variant="ghost" size="sm">
                    Follow
                  </Button>
                </div>
              ))}
            </CardContent>
            <CardFooter>
              <Button variant="outline" className="w-full">
                View All Contributors
              </Button>
            </CardFooter>
          </Card>

          {/* Create your own */}
          <Card className="bg-gradient-to-br from-blue-50 to-purple-50 dark:from-blue-950/50 dark:to-purple-950/50">
            <CardHeader>
              <CardTitle className="text-lg">Create Your Own List</CardTitle>
              <CardDescription>
                Share your knowledge with the community by creating and
                publishing your vocabulary lists
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="flex items-center justify-center space-x-2 py-4">
                <ThumbsUp className="h-5 w-5 text-blue-500" />
                <Share className="h-5 w-5 text-purple-500" />
                <BookOpen className="h-5 w-5 text-green-500" />
              </div>
            </CardContent>
            <CardFooter>
              <Button
                className="w-full"
                onClick={() =>
                  navigate(
                    ROUTES.LEARNING.LEARNING_VOCABULARY.replace(
                      ":id",
                      "flash-card"
                    )
                  )
                }
              >
                Start Creating
              </Button>
            </CardFooter>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default CommunityVocabularyContainer;

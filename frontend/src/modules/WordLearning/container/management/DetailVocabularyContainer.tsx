import React, { useEffect } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import {
  ChevronLeft,
  Volume,
  ExternalLink,
  Bookmark,
  BookmarkPlus,
  Star,
} from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
  CardFooter,
} from "@/shared/components/ui/card";
import {
  Tabs,
  TabsList,
  TabsTrigger,
  TabsContent,
} from "@/shared/components/ui/tabs";
import { Separator } from "@/shared/components/ui/separator";
import { ROUTES } from "@/core/routes/constants";
import { useGetVocabularySetByIdQuery } from "../../api/vocabularySetApi";
import { Toast } from "@/shared/components/ui/toast";

// Demo word data - would come from API in real implementation
const VocabularyData = {
  id: "1",
  word: "Abundant",
  pronunciation: "/əˈbʌndənt/",
  partOfSpeech: "adjective",
  meaning: "Existing or available in large quantities; plentiful",
  example: "The region has abundant natural resources.",
  etymology: "From Latin abundans (overflowing), from abundare (to overflow)",
  synonyms: ["plentiful", "copious", "ample", "profuse", "rich", "lavish"],
  antonyms: ["scarce", "meager", "insufficient", "sparse", "rare"],
  difficulty: "medium",
  status: "learning",
  notes: "",
  usage: [
    "Tropical rainforests are known for their abundant biodiversity.",
    "The company reported abundant profits in the last quarter.",
    "She has abundant patience when dealing with young children.",
  ],
  relatedWords: [
    {
      word: "Abundance",
      partOfSpeech: "noun",
      meaning: "A very large quantity of something",
    },
    {
      word: "Abundantly",
      partOfSpeech: "adverb",
      meaning: "In large quantities; plentifully",
    },
  ],
  tags: ["TOEFL", "Academic", "Nature"],
};

const DetailVocabularyContainer = () => {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [isSaved, setIsSaved] = React.useState(false);

  // In a real app, we would fetch the word data based on the ID
  // const { data: word, isLoading } = useQuery({
  //   queryKey: ['word', id],
  //   queryFn: () => fetchWordById(id)
  // });

  const { data, isLoading, error } = useGetVocabularySetByIdQuery({
    id: id as string,
  });
  const handleSaveWord = () => {
    setIsSaved(!isSaved);
    // In a real app, we would call an API to save/unsave the word
  };
  useEffect(() => {
    if (error) {
      // Handle error (e.g., show a notification or redirect)
      console.error("Error fetching word data:", error);
      Toast({
        title: "Error",
        variant: "destructive",
        content: "Failed to fetch word data. Please try again.",
      });
    }
  }, [error]);
  if (isLoading) return <div>Loading...</div>;
  return (
    <div className="container py-8 max-w-4xl mx-auto">
      {/* Back button and header */}
      <div className="mb-8">
        <Button
          variant="ghost"
          size="sm"
          className="mb-4"
          onClick={() => navigate(-1)}
        >
          <ChevronLeft className="h-4 w-4 mr-2" /> Back to Vocabulary List
        </Button>

        <div className="flex flex-col md:flex-row justify-between gap-4 items-start">
          <div className="flex-1">
            <div className="flex items-center gap-2">
              <h1 className="text-3xl font-bold">{VocabularyData.word}</h1>
              <Button variant="ghost" size="icon" className="h-8 w-8">
                <Volume className="h-4 w-4" />
              </Button>
            </div>
            <div className="text-muted-foreground mb-1">
              {VocabularyData.pronunciation} • {VocabularyData.partOfSpeech}
            </div>
            <div className="flex gap-2 mt-2">
              {VocabularyData.tags.map((tag) => (
                <div
                  key={tag}
                  className="inline-block px-2 py-1 text-xs font-medium rounded-full bg-blue-100 text-blue-800 dark:bg-blue-900/30 dark:text-blue-400"
                >
                  {tag}
                </div>
              ))}
            </div>
          </div>
          <div className="flex gap-2 self-start">
            <Button variant="outline" size="sm" onClick={handleSaveWord}>
              {isSaved ? (
                <>
                  <Bookmark className="h-4 w-4 mr-2 fill-current" /> Saved
                </>
              ) : (
                <>
                  <BookmarkPlus className="h-4 w-4 mr-2" /> Save
                </>
              )}
            </Button>
            <Button
              size="sm"
              onClick={() =>
                navigate(
                  ROUTES.LEARNING.LEARNING_VOCABULARY.replace(
                    ":id",
                    "flash-card"
                  )
                )
              }
            >
              Practice
            </Button>
          </div>
        </div>
      </div>

      {/* Word content */}
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-2 space-y-6">
          {/* Main definition card */}
          <Card>
            <CardHeader>
              <CardTitle>Definition</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="prose dark:prose-invert max-w-none">
                <p className="text-lg">{VocabularyData.meaning}</p>
              </div>

              <div className="pt-2">
                <h4 className="font-medium mb-2">Examples</h4>
                <ul className="list-disc pl-5 space-y-1">
                  <li>{VocabularyData.example}</li>
                  {VocabularyData.usage.map((example, index) => (
                    <li key={index}>{example}</li>
                  ))}
                </ul>
              </div>
            </CardContent>
          </Card>

          {/* Additional information tabs */}
          <Tabs defaultValue="etymology" className="w-full">
            <TabsList className="grid w-full grid-cols-3">
              <TabsTrigger value="etymology">Etymology</TabsTrigger>
              <TabsTrigger value="related">Related Words</TabsTrigger>
              <TabsTrigger value="notes">My Notes</TabsTrigger>
            </TabsList>

            <TabsContent value="etymology" className="space-y-4 mt-4">
              <Card>
                <CardHeader>
                  <CardTitle>Etymology</CardTitle>
                  <CardDescription>
                    The origin and historical development of this word
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <p>{VocabularyData.etymology}</p>
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="related" className="space-y-4 mt-4">
              <Card>
                <CardHeader>
                  <CardTitle>Related Words</CardTitle>
                  <CardDescription>
                    Words derived from the same root or related in meaning
                  </CardDescription>
                </CardHeader>
                <CardContent className="space-y-4">
                  {VocabularyData.relatedWords.map((relatedWord, index) => (
                    <div key={index} className="space-y-1">
                      <div className="flex items-center">
                        <span className="font-medium">{relatedWord.word}</span>
                        <span className="text-sm text-muted-foreground ml-2">
                          ({relatedWord.partOfSpeech})
                        </span>
                      </div>
                      <p className="text-sm">{relatedWord.meaning}</p>
                      {index < VocabularyData.relatedWords.length - 1 && (
                        <Separator className="my-2" />
                      )}
                    </div>
                  ))}
                </CardContent>
              </Card>
            </TabsContent>

            <TabsContent value="notes" className="space-y-4 mt-4">
              <Card>
                <CardHeader>
                  <CardTitle>My Notes</CardTitle>
                  <CardDescription>
                    Your personal notes about this word
                  </CardDescription>
                </CardHeader>
                <CardContent>
                  <textarea
                    className="w-full min-h-[150px] p-3 rounded-md border border-border bg-background"
                    placeholder="Add your notes about this word here..."
                    defaultValue={VocabularyData.notes}
                  />
                </CardContent>
                <CardFooter>
                  <Button className="ml-auto">Save Notes</Button>
                </CardFooter>
              </Card>
            </TabsContent>
          </Tabs>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Synonyms and Antonyms card */}
          <Card>
            <CardHeader>
              <CardTitle>Synonyms & Antonyms</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <div>
                <h4 className="font-medium mb-2">Synonyms</h4>
                <div className="flex flex-wrap gap-2">
                  {VocabularyData.synonyms.map((synonym) => (
                    <div
                      key={synonym}
                      className="px-3 py-1 rounded-md bg-muted text-sm"
                    >
                      {synonym}
                    </div>
                  ))}
                </div>
              </div>

              <div>
                <h4 className="font-medium mb-2">Antonyms</h4>
                <div className="flex flex-wrap gap-2">
                  {VocabularyData.antonyms.map((antonym) => (
                    <div
                      key={antonym}
                      className="px-3 py-1 rounded-md bg-muted text-sm"
                    >
                      {antonym}
                    </div>
                  ))}
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Difficulty level */}
          <Card>
            <CardHeader>
              <CardTitle>Difficulty Level</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center justify-center py-2">
                {VocabularyData.difficulty === "easy" && (
                  <div className="flex">
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                    <Star className="h-6 w-6 text-muted" />
                    <Star className="h-6 w-6 text-muted" />
                  </div>
                )}
                {VocabularyData.difficulty === "medium" && (
                  <div className="flex">
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                    <Star className="h-6 w-6 text-muted" />
                  </div>
                )}
                {VocabularyData.difficulty === "hard" && (
                  <div className="flex">
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                    <Star className="h-6 w-6 fill-yellow-400 text-yellow-400" />
                  </div>
                )}
              </div>
              <div className="text-center text-sm text-muted-foreground mt-1">
                {VocabularyData.difficulty.charAt(0).toUpperCase() +
                  VocabularyData.difficulty.slice(1)}{" "}
                difficulty
              </div>
            </CardContent>
          </Card>

          {/* External resources */}
          <Card>
            <CardHeader>
              <CardTitle>External Resources</CardTitle>
            </CardHeader>
            <CardContent className="space-y-2">
              <Button
                variant="outline"
                className="w-full justify-start"
                asChild
              >
                <a
                  href={`https://www.merriam-webster.com/dictionary/${VocabularyData.word.toLowerCase()}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <ExternalLink className="h-4 w-4 mr-2" /> Merriam-Webster
                </a>
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                asChild
              >
                <a
                  href={`https://dictionary.cambridge.org/dictionary/english/${VocabularyData.word.toLowerCase()}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <ExternalLink className="h-4 w-4 mr-2" /> Cambridge Dictionary
                </a>
              </Button>
              <Button
                variant="outline"
                className="w-full justify-start"
                asChild
              >
                <a
                  href={`https://www.thesaurus.com/browse/${VocabularyData.word.toLowerCase()}`}
                  target="_blank"
                  rel="noopener noreferrer"
                >
                  <ExternalLink className="h-4 w-4 mr-2" /> Thesaurus.com
                </a>
              </Button>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default DetailVocabularyContainer;

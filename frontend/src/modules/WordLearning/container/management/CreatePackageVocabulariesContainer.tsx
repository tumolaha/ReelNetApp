import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  ChevronLeft,
  Plus,
  Trash2,
  Save,
  Upload,
  Download,
  HelpCircle,
  AlertCircle,
} from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import { Input } from "@/shared/components/ui/input";
import { Textarea } from "@/shared/components/ui/textarea";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
  CardFooter,
} from "@/shared/components/ui/card";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/shared/components/ui/table";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shared/components/ui/select";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shared/components/ui/form";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/shared/components/ui/tooltip";
import { useToast } from "@/shared/components/ui/use-toast";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import * as z from "zod";
import { useCreateVocabularySetMutation } from "../../api/vocabularySetApi";
import { ROUTES } from "@/core/routes/constants";
import { CATEGORY_LIST } from "../../constants/category.constants";

const formSchema = z.object({
  title: z.string().min(3, {
    message: "Title must be at least 3 characters.",
  }),
  description: z.string().min(10, {
    message: "Description must be at least 10 characters.",
  }),
  category: z.string({
    required_error: "Please select a category.",
  }),
  visibility: z.enum(["PUBLIC", "PRIVATE"], {
    required_error: "Please select visibility.",
  }),
});



const CreatePackageVocabulariesContainer = () => {
  const navigate = useNavigate();
  const { toast } = useToast();
  const [words, setWords] = useState([
    { id: 1, word: "", meaning: "", example: "", partOfSpeech: "noun" },
  ]);
  const [createVocabularySet] = useCreateVocabularySetMutation();

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      title: "",
      description: "",
      category: "",
      visibility: "PRIVATE",
    },
  });

  const addWord = () => {
    const newId =
      words.length > 0 ? Math.max(...words.map((w) => w.id)) + 1 : 1;
    setWords([
      ...words,
      { id: newId, word: "", meaning: "", example: "", partOfSpeech: "noun" },
    ]);
  };

  const removeWord = (id: number) => {
    if (words.length > 1) {
      setWords(words.filter((word) => word.id !== id));
    } else {
      toast({
        title: "Cannot remove all words",
        description: "Your vocabulary list must have at least one word.",
        variant: "destructive",
      });
    }
  };

  const updateWord = (id: number, field: string, value: string) => {
    setWords(
      words.map((word) => (word.id === id ? { ...word, [field]: value } : word))
    );
  };

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    
    // Validate that all words have content
    const emptyWords = words.filter((word) => !word.word || !word.meaning);
    if (emptyWords.length > 0) {
      toast({
        title: "Incomplete words",
        description:
          "Please fill in all word and meaning fields before saving.",
        variant: "destructive",
      });
      return;
    }

    // In a real app, we would send the form data and words to an API
    console.log("Form data:", values);
    console.log("Words:", words);

    try {
      await createVocabularySet({
        name: values.title,
        description: values.description,
        visibility: values.visibility,
        category: values.category,
        

      }).unwrap();
      // Handle successful creation
      toast({
        title: "Vocabulary list created!",
        description: "Your vocabulary list has been successfully saved.",
      });
      // Navigate to the personal vocabulary page
      navigate(ROUTES.VOCABULARIES.MY_VOCABULARIES);
    } catch (error) {
      toast({
        title: "Error creating vocabulary list",
        description: "There was an error creating your vocabulary list.",
        variant: "destructive",
      });
      return;
    }
  };

  const importFromCSV = () => {
    // This would be implemented with a file upload in a real app
    toast({
      title: "CSV Import",
      description: "This feature would allow importing words from a CSV file.",
    });
  };

  const downloadTemplate = () => {
    // This would download a CSV template in a real app
    toast({
      title: "Template Downloaded",
      description: "CSV template for vocabulary import has been downloaded.",
    });
  };

  return (
    <div className="container py-8 max-w-5xl mx-auto">
      {/* Back button and header */}
      <div className="mb-8">
        <Button
          variant="ghost"
          size="sm"
          className="mb-4"
          onClick={() => navigate(-1)}
        >
          <ChevronLeft className="h-4 w-4 mr-2" /> Back to My Vocabulary
        </Button>

        <div className="flex items-center justify-between">
          <h1 className="text-3xl font-bold">Create Vocabulary List</h1>
        </div>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-2 space-y-6">
          {/* List details form */}
          <Card>
            <CardHeader>
              <CardTitle>List Details</CardTitle>
              <CardDescription>
                Basic information about your vocabulary list
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Form {...form}>
                <form className="space-y-4">
                  <FormField
                    control={form.control}
                    name="title"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Title</FormLabel>
                        <FormControl>
                          <Input
                            placeholder="e.g., Essential Business Vocabulary"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <FormField
                    control={form.control}
                    name="description"
                    render={({ field }) => (
                      <FormItem>
                        <FormLabel>Description</FormLabel>
                        <FormControl>
                          <Textarea
                            placeholder="Describe what this vocabulary list is about..."
                            className="min-h-[100px]"
                            {...field}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />

                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <FormField
                      control={form.control}
                      name="category"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>Category</FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            defaultValue={field.value}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Select a category" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              {CATEGORY_LIST.map((category) => (
                                <SelectItem
                                  key={category.value}
                                  value={category.value}
                                >
                                  {category.label}
                                </SelectItem>
                              ))}
                            </SelectContent>
                          </Select>
                          <FormMessage />
                        </FormItem>
                      )}
                    />

                    <FormField
                      control={form.control}
                      name="visibility"
                      render={({ field }) => (
                        <FormItem>
                          <FormLabel>
                            Visibility
                            <TooltipProvider>
                              <Tooltip>
                                <TooltipTrigger asChild>
                                  <Button
                                    variant="ghost"
                                    size="icon"
                                    className="h-4 w-4 ml-1"
                                  >
                                    <HelpCircle className="h-3 w-3" />
                                  </Button>
                                </TooltipTrigger>
                                <TooltipContent>
                                  <p>
                                    Public: Visible to all users
                                    <br />
                                    Private: Only visible to you
                                  </p>
                                </TooltipContent>
                              </Tooltip>
                            </TooltipProvider>
                          </FormLabel>
                          <Select
                            onValueChange={field.onChange}
                            defaultValue={field.value}
                          >
                            <FormControl>
                              <SelectTrigger>
                                <SelectValue placeholder="Select visibility" />
                              </SelectTrigger>
                            </FormControl>
                            <SelectContent>
                              <SelectItem value="PUBLIC">Public</SelectItem>
                              <SelectItem value="PRIVATE">Private</SelectItem>
                            </SelectContent>
                          </Select>
                          <FormMessage />
                        </FormItem>
                      )}
                    />
                  </div>
                </form>
              </Form>
            </CardContent>
          </Card>

          {/* Words list */}
          <Card>
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle>Words</CardTitle>
                  <CardDescription>
                    Add words to your vocabulary list
                  </CardDescription>
                </div>
                <Button variant="outline" size="sm" onClick={addWord}>
                  <Plus className="h-4 w-4 mr-2" /> Add Word
                </Button>
              </div>
            </CardHeader>
            <CardContent>
              <div className="rounded-md border">
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead className="w-[180px]">Word</TableHead>
                      <TableHead>Meaning</TableHead>
                      <TableHead className="w-[120px]">
                        Part of Speech
                      </TableHead>
                      <TableHead className="w-[50px]"></TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {words.map((word) => (
                      <TableRow key={word.id}>
                        <TableCell>
                          <Input
                            value={word.word}
                            onChange={(e) =>
                              updateWord(word.id, "word", e.target.value)
                            }
                            placeholder="Enter word"
                          />
                        </TableCell>
                        <TableCell>
                          <Input
                            value={word.meaning}
                            onChange={(e) =>
                              updateWord(word.id, "meaning", e.target.value)
                            }
                            placeholder="Enter meaning"
                          />
                        </TableCell>
                        <TableCell>
                          <Select
                            value={word.partOfSpeech}
                            onValueChange={(value) =>
                              updateWord(word.id, "partOfSpeech", value)
                            }
                          >
                            <SelectTrigger>
                              <SelectValue />
                            </SelectTrigger>
                            <SelectContent>
                              <SelectItem value="noun">Noun</SelectItem>
                              <SelectItem value="verb">Verb</SelectItem>
                              <SelectItem value="adjective">
                                Adjective
                              </SelectItem>
                              <SelectItem value="adverb">Adverb</SelectItem>
                              <SelectItem value="preposition">
                                Preposition
                              </SelectItem>
                              <SelectItem value="conjunction">
                                Conjunction
                              </SelectItem>
                              <SelectItem value="pronoun">Pronoun</SelectItem>
                              <SelectItem value="interjection">
                                Interjection
                              </SelectItem>
                            </SelectContent>
                          </Select>
                        </TableCell>
                        <TableCell>
                          <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => removeWord(word.id)}
                          >
                            <Trash2 className="h-4 w-4 text-destructive" />
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                    {words.length === 0 && (
                      <TableRow>
                        <TableCell
                          colSpan={4}
                          className="text-center py-8 text-muted-foreground"
                        >
                          No words added yet
                        </TableCell>
                      </TableRow>
                    )}
                  </TableBody>
                </Table>
              </div>

              <div className="flex justify-center mt-4">
                <Button
                  variant="outline"
                  className="w-full md:w-auto"
                  onClick={addWord}
                >
                  <Plus className="h-4 w-4 mr-2" /> Add Another Word
                </Button>
              </div>
            </CardContent>
            <CardFooter className="flex justify-between gap-2 flex-wrap">
              <div className="flex gap-2">
                <Button variant="outline" size="sm" onClick={importFromCSV}>
                  <Upload className="h-4 w-4 mr-2" /> Import from CSV
                </Button>
                <Button variant="outline" size="sm" onClick={downloadTemplate}>
                  <Download className="h-4 w-4 mr-2" /> Get CSV Template
                </Button>
              </div>
              <div className="text-sm text-muted-foreground flex items-center">
                <AlertCircle className="h-4 w-4 mr-1" /> Words added:{" "}
                {words.length}
              </div>
            </CardFooter>
          </Card>
        </div>

        {/* Sidebar */}
        <div className="space-y-6">
          {/* Save actions */}
          <Card>
            <CardHeader>
              <CardTitle>Save & Publish</CardTitle>
            </CardHeader>
            <CardContent className="space-y-4">
              <Button className="w-full" onClick={form.handleSubmit(onSubmit)}>
                <Save className="h-4 w-4 mr-2" /> Save Vocabulary List
              </Button>
              <div className="text-sm text-muted-foreground">
                You can edit this list anytime after saving.
              </div>
            </CardContent>
          </Card>

          {/* Tips card */}
          <Card>
            <CardHeader>
              <CardTitle>Tips</CardTitle>
            </CardHeader>
            <CardContent>
              <ul className="list-disc pl-5 space-y-2 text-sm">
                <li>Include clear and concise definitions</li>
                <li>Add example sentences to show context</li>
                <li>Group related words together</li>
                <li>Include common variations or forms</li>
                <li>Import from CSV for bulk upload</li>
              </ul>
            </CardContent>
          </Card>

          {/* Example list structure */}
          <Card>
            <CardHeader>
              <CardTitle>Example Structure</CardTitle>
            </CardHeader>
            <CardContent className="text-sm">
              <p className="mb-2">A good vocabulary list includes:</p>
              <ol className="list-decimal pl-5 space-y-1">
                <li>Clear word entries</li>
                <li>Accurate definitions</li>
                <li>Correct part of speech</li>
                <li>Example sentences</li>
                <li>Synonyms & antonyms (optional)</li>
              </ol>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default CreatePackageVocabulariesContainer;

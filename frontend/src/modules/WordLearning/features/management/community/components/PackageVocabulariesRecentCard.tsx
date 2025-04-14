import { BookOpen, Clock, Users, Heart } from "lucide-react";
import { Button } from "@/shared/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardTitle,
  CardHeader,
} from "@/shared/components/ui/card";
import { Download } from "lucide-react";
import { Link } from "react-router-dom";
import { ROUTES } from "@/core/routes/constants";
import { VocabularySetType } from "@/modules/WordLearning/types/vocabularySet.types";

interface PackageVocabulariesRecentCardProps {
  set: VocabularySetType;
}
const PackageVocabulariesRecentCard = ({
  set,
}: PackageVocabulariesRecentCardProps) => {
  return (
    <Card key={set.id}>
      <CardHeader>
        <div className="flex justify-between">
          <div>
            <CardTitle>{set.name}</CardTitle>
            <CardDescription>{set.description}</CardDescription>
          </div>
          <div className="text-sm text-muted-foreground">
            <Clock className="h-3 w-3 inline mr-1" />
            {new Date(set.createdAt).toLocaleDateString()}
            {/* Format the date for better readability */}
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <div className="flex items-center text-sm text-muted-foreground mb-3">
          <Users className="h-4 w-4 mr-1" />
          <span className="mr-4">By {set.createdBy}</span>{" "}
          {/* Updated to use createdBy instead of userCreated */}
          <BookOpen className="h-4 w-4 mr-1" />
          <span>{set.itemCount} words</span>{" "}
          {/* Updated to use itemCount instead of numberOfWords */}
          <span className="mx-3">•</span>
          <span>{set.category}</span>{" "}
          {/* Updated to use language instead of languages */}
        </div>
      </CardContent>
      <CardFooter className="flex justify-between">
        <div className="flex items-center text-sm">
          <Heart className="h-4 w-4 mr-1 text-red-500" />
          <span className="mr-3">{set.likeCount}</span>
          <Download className="h-4 w-4 mr-1" />
          <span>{set.shareCount}</span>{" "}
          {/* Updated to use downloadCount instead of downloads */}
        </div>
        <div className="flex gap-2">
          <Button variant="outline" size="sm">
            <Download className="h-4 w-4 mr-1" /> Add
          </Button>
          <Button size="sm" asChild>
            <Link
              to={ROUTES.VOCABULARIES.OVERVIEW_DETAIL_PACKAGE_VOCABULARY.replace(
                ":id",
                set.id
              )}
            >
              View
            </Link>
          </Button>
        </div>
      </CardFooter>
    </Card>
  );
};

export default PackageVocabulariesRecentCard;

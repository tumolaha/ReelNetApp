import { Link } from "react-router-dom";
import { Button } from "@/shared/components/ui/button";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
  CardFooter,
} from "@/shared/components/ui/card";
import { BookOpen, ChevronRight, Download, Heart, Users } from "lucide-react";
import { ROUTES } from "@/core/routes/constants";
import { VocabularySetType } from "@/modules/WordLearning/types/vocabularySet.types";
import imageCatalog, { createImageExport } from "@/shared/assets/images/images";

const placeholderImage = createImageExport(
  imageCatalog.defaultVocabularySet1,
  "Vocabulary package cover image"
);
interface PackageVocabulariesCardProps {
  set: VocabularySetType;
}
const PackageVocabulariesCard = ({ set }: PackageVocabulariesCardProps) => {
  return (
    <Card key={set.id} className="overflow-hidden">
      <div className="flex flex-col md:flex-row">
        <div className="w-full md:w-1/3 h-48 md:h-auto">
          <img
            src={set.image || placeholderImage.src}
            alt={set.name || placeholderImage.alt}
            className="w-full h-full object-cover"
          />
        </div>
        <div className="flex-1 p-6">
          <CardHeader className="p-0 pb-4">
            <div className="flex justify-between items-start">
              <div>
                <CardTitle className="text-xl">{set.name}</CardTitle>
                <CardDescription>{set.description}</CardDescription>
              </div>
              <Button variant="outline" size="sm">
                <Download className="h-4 w-4 mr-1" /> Add
              </Button>
            </div>
          </CardHeader>
          <CardContent className="p-0 pb-4">
            <div className="flex items-center text-sm text-muted-foreground mb-2">
              <Users className="h-4 w-4 mr-1" />
              <span className="mr-4">By {set.createdBy}</span>
              <BookOpen className="h-4 w-4 mr-1" />
              <span>{set.itemCount} words</span>
            </div>
            <div className="flex flex-wrap gap-2">
              {set.category || "General"}
            </div>
          </CardContent>
          <CardFooter className="p-0 pt-2 flex justify-between">
            <div className="flex items-center text-sm">
              <Heart className="h-4 w-4 mr-1 text-red-500" />
              <span className="mr-3">{set.likeCount}</span>
              <Download className="h-4 w-4 mr-1" />
              <span>{set.shareCount}</span>
            </div>
            <Button variant="ghost" size="sm" asChild>
              <Link
                to={`${ROUTES.VOCABULARIES.OVERVIEW_DETAIL_PACKAGE_VOCABULARY.replace(
                  ":id",
                  set.id
                )}`}
              >
                View Details <ChevronRight className="h-4 w-4 ml-1" />
              </Link>
            </Button>
          </CardFooter>
        </div>
      </div>
    </Card>
  );
};

export default PackageVocabulariesCard;

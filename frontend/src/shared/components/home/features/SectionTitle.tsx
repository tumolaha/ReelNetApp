import { motion } from 'framer-motion';

interface SectionTitleProps {
  title: string;
  highlight: string;
  description: string;
}

export function SectionTitle({ title, highlight, description }: SectionTitleProps) {
  return (
    <motion.div 
      className="text-center max-w-2xl mx-auto mb-16"
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5 }}
    >
      <h2 className="text-3xl font-bold mb-4">
        {title} <span className="gradient-text">{highlight}</span>
      </h2>
      <p className="text-gray-600">
        {description}
      </p>
    </motion.div>
  );
}
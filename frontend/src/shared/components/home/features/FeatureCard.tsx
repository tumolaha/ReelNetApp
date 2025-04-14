import { Card } from 'flowbite-react';
import { IconType } from 'react-icons';
import { motion } from 'framer-motion';

interface FeatureCardProps {
  title: string;
  description: string;
  image: string;
  icon: IconType;
  gradient: string;
  index: number;
}

export function FeatureCard({ title, description, image, icon: Icon, gradient, index }: FeatureCardProps) {
  return (
    <motion.div
      initial={{ opacity: 0, y: 50 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.5, delay: index * 0.1 }}
    >
      <Card className="feature-card border-0 overflow-hidden bg-white hover:shadow-2xl transition-all duration-300">
        <div className="relative group">
          <div className="absolute inset-0 bg-gradient-to-b from-transparent to-black/50 opacity-0 group-hover:opacity-100 transition-opacity duration-300" />
          <img 
            src={image} 
            alt={title}
            className="w-full h-48 object-cover rounded-t-lg transform group-hover:scale-105 transition-transform duration-500"
          />
          <div className={`absolute -bottom-8 left-1/2 -translate-x-1/2 w-16 h-16 rounded-full flex items-center justify-center bg-gradient-to-r ${gradient} shadow-lg border-4 border-white group-hover:scale-110 transition-transform duration-300`}>
            <Icon className="w-8 h-8 text-white" />
          </div>
        </div>
        <div className="flex flex-col items-center text-center mt-12 p-6">
          <h3 className="text-xl font-semibold mb-3 bg-clip-text text-transparent bg-gradient-to-r from-gray-900 to-gray-600">
            {title}
          </h3>
          <p className="text-gray-600 leading-relaxed">
            {description}
          </p>
        </div>
      </Card>
    </motion.div>
  );
}
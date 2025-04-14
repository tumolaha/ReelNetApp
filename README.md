# ReelNet - AI-Powered English Learning Platform

![ReelNet Logo](assets/logo.png)

[![Build Status](https://github.com/reelnet/reelnet-app/actions/workflows/ci.yml/badge.svg)](https://github.com/reelnet/reelnet-app/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Documentation](https://img.shields.io/badge/docs-architecture-blue)](docs/architecture.md)

ReelNet is a comprehensive English learning platform that leverages AI technology to enhance reading, listening, speaking, and writing skills. The platform provides personalized learning experiences through adaptive content and real-time feedback.

## 🌟 Features

- **Reading Module**: Interactive reading exercises with comprehension tests
- **Listening Module**: Audio-based learning with transcription and pronunciation practice
- **Speaking Module**: AI-powered pronunciation assessment and feedback
- **Writing Module**: Writing exercises with grammar and style analysis
- **Vocabulary Learning**: Contextual vocabulary building with spaced repetition
- **Exam Preparation**: Specialized modules for TOEFL, IELTS, and other English proficiency tests
- **Community Features**: Discussion forums and peer learning opportunities

## 🚀 Getting Started

### Prerequisites

- Node.js (v16.x or higher)
- Java JDK 17
- PostgreSQL 14+
- Redis 6+
- Docker and Docker Compose (for containerized deployment)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/reelnet/reelnet-app.git
   cd reelnet-app
   ```

2. **Set up environment variables**
   ```bash
   # Frontend
   cp frontend/.env.example frontend/.env
   
   # Backend
   cp backend/.env.example backend/.env
   ```

3. **Install dependencies**
   ```bash
   # Frontend
   cd frontend
   npm install
   
   # Backend
   cd ../backend
   ./mvnw clean install
   ```

4. **Start the development environment**
   ```bash
   # Start the database and Redis
   docker-compose up -d postgres redis
   
   # Start the backend
   cd backend
   ./mvnw spring-boot:run
   
   # Start the frontend (in a new terminal)
   cd frontend
   npm start
   ```

5. **Access the application**
   Open your browser and navigate to `http://localhost:3000`

## 🏗️ Architecture

ReelNet follows a Modular Monolith architecture with a clear roadmap for transitioning to Microservices as the system evolves. For detailed architecture information, see the [Architecture Documentation](docs/architecture.md).

### Key Components

- **Frontend**: React, TypeScript, Tailwind CSS
- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Caching**: Redis
- **AI Services**: Speech Recognition, NLP, Recommendation Engine
- **Authentication**: Auth0

## 🧪 Testing

```bash
# Frontend tests
cd frontend
npm test

# Backend tests
cd backend
./mvnw test
```

## 🚀 Deployment

### Production Build

```bash
# Frontend build
cd frontend
npm run build

# Backend build
cd backend
./mvnw clean package -Pprod
```

### Docker Deployment

```bash
# Build and run with Docker Compose
docker-compose up -d
```

## 📚 Documentation

- [Architecture Documentation](docs/architecture.md)
- [API Documentation](docs/api.md)
- [User Guide](docs/user-guide.md)
- [Contributing Guidelines](CONTRIBUTING.md)

## 🛣️ Roadmap

### Phase 1: MVP (Current)
- Core learning modules implementation
- Basic AI integration
- User authentication and profiles

### Phase 2: Enhanced Features
- Advanced AI capabilities
- Mobile application
- Offline learning mode

### Phase 3: Community and Social
- Enhanced community features
- Gamification elements
- Social learning tools

### Phase 4: Enterprise Features
- Team learning environments
- Corporate training modules
- Advanced analytics and reporting

## 🤝 Contributing

We welcome contributions to ReelNet! Please read our [Contributing Guidelines](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Contact

- **Website**: [reelnet.com](https://reelnet.com)
- **Email**: contact@reelnet.com
- **Twitter**: [@ReelNetApp](https://twitter.com/ReelNetApp)
- **LinkedIn**: [ReelNet](https://linkedin.com/company/reelnet)

---

<p align="center">Made with ❤️ by the ReelNet Team</p>
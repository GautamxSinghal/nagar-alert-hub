# 🏙️ Nagar Alert Hub v4.0 - Live City
> **Real-Time Community-Integrated Public Disruption Intelligence System**

## 📋 Overview
Nagar Alert Hub is a comprehensive alert management system designed to keep citizens informed about public disruptions, emergencies, and important city announcements in real-time. Version 4.0 introduces **Live City** features for enhanced real-time monitoring and instant notifications across multiple channels.

## 🚀 Live Demo
**Click here to test the app:** [Live Deployment Link](https://nagaralerthub.up.railway.app/)
*(Note: It is hosted on a free tier, so it may take 50 seconds to wake up initially.)*

## ✨ Version 4.0 - Live City Features
- **Real-Time Alert Dashboard** - Live updates of alerts across your city
- **Multi-Channel Notifications** - WhatsApp, SMS, and in-app alerts
- **Smart Alert Routing** - Intelligent categorization and routing based on severity and location
- **Citizen Engagement** - Community-driven alert reporting and verification
- **Admin Dashboard** - Comprehensive management and monitoring tools
- **Accessibility First** - WCAG 2.1 compliant UI for inclusive access

## 🎯 Key Features
### For Citizens
- 📱 Receive real-time alerts about disruptions
- 🔔 Customize notification preferences
- 📍 Location-based alert filtering
- ⭐ Rate and verify alert accuracy
- 💬 Report new disruptions to the community

### For Administrators
- 📊 Comprehensive alert management dashboard
- 🎯 Create and schedule alerts
- 📈 Track alert engagement metrics
- 👥 Manage user accounts and permissions
- 🔍 Monitor system health and performance

## 🛠️ Technology Stack
- **Backend**: Java Spring Boot
- **Frontend**: HTML5, CSS3, JavaScript
- **Database**: MongoDB
- **Notifications**: WhatsApp API, Twilio
- **Deployment**: Railway.app

## 📦 Prerequisites
- Java 17+
- Maven 3.8+
- MongoDB instance
- Twilio account for WhatsApp notifications

## 🚀 Getting Started

### Installation
```bash
# Clone the repository
git clone https://github.com/GautamxSinghal/nagar-alert-hub.git

# Navigate to project directory
cd nagar-alert-hub

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Configuration
1. Configure your MongoDB connection in `application.properties`
2. Add Twilio credentials for WhatsApp notifications
3. Update base URL and other settings as needed

### Access the Application
- **URL**: http://localhost:8080
- **Admin Dashboard**: http://localhost:8080/admin
- **Login**: Use default credentials (configured in application.properties)

## 🔐 Security Features
- User authentication and authorization
- Role-based access control (RBAC)
- Input validation and sanitization
- CSRF protection
- Secure password storage

## 📱 API Endpoints
### Public Endpoints
- `GET /api/alerts` - Get all active alerts
- `GET /api/alerts/{id}` - Get specific alert details
- `POST /api/alerts/report` - Report a new disruption

### Protected Endpoints
- `POST /api/alerts/create` - Create new alert (Admin)
- `PUT /api/alerts/{id}` - Update alert (Admin)
- `DELETE /api/alerts/{id}` - Delete alert (Admin)
- `GET /api/users` - Manage users (Admin)

## 🤝 Contributing
We welcome contributions! Please follow these steps:
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/improvement`)
3. Commit your changes (`git commit -m 'Add improvement'`)
4. Push to the branch (`git push origin feature/improvement`)
5. Open a Pull Request

## 📄 License
This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support & Contact
- **Issues**: Report bugs on [GitHub Issues](https://github.com/GautamxSinghal/nagar-alert-hub/issues)
- **Email**: support@nagaralerthub.com
- **Community**: Join our discussions on GitHub

## 🙏 Acknowledgments
- Special thanks to all contributors and community members
- Inspired by the need for better civic communication
- Built with ❤️ for our communities

---
**Version**: 4.0 - Live City  
**Last Updated**: July 2026  
**Maintained by**: Nagar Alert Hub Team

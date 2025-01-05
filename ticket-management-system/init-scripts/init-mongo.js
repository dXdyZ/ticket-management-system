db = db.getSiblingDB('ticket_bd'); // Переключаемся на базу данных message_bd

db.createUser({
    user: "app_user",
    pwd: "ticket_bd",
    roles: [
        {
            role: "readWrite",
            db: "ticket_bd"
        }
    ]
});

print("User and database created successfully!");

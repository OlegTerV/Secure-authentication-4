# Secure-authentication-4

В рамках данной лабораторной работы на основе* приложения из Лабораторной №3* (№2*) требуется:

*При особых затруднениях допускается использование финального готового кода проекта из Google Codelab для 2 лабы

Реализовать шифрование базы данных средствами SQLCipher
Предусмотреть конвертации базы из нешифрованной в зашифрованную в случае, если обнаружена незашифрованная база
Для шифрования базы необходимо использовать ключ, хранимый в Android KeyStore
Примечания:

SQLCipher для Room лежит [тут](https://github.com/commonsguy/cwac-saferoom/tree/v1.2.1), а SQLCipherUtils (для ознакомления) - [тут](https://github.com/commonsguy/cwac-saferoom/blob/v1.2.1/saferoom/src/main/java/com/commonsware/cwac/saferoom/SQLCipherUtils.java#L36-L86)
Либа для SupportFactory лежит [тут](https://github.com/sqlcipher/android-database-sqlcipher)

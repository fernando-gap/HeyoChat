# Heyo Chat

O principal foco foi no backend, no entanto, o frontend esta completemante ruim e sem sal.
The focus was mainly on the backend, so the frontend is completely wack and is missing some polishing.

Ha multiplos problemas de design do banco de dados: 
 1) usuarios nao possuem uma lista de contatos, o que fez ser determinada pela tabela de Mensagens apenas.
 2) toda mensagem e armazenada (incluindo grupos)
 3) nao ha como tracar (contatos) de forma simples

Outra coisa foi o fato do frontend nao ser desaclopado entre diversos componentes separados, ou seja, a maioria estao localizados em gui.ScreenRoot.java


Lista de bugs (atualmente):
- [ ] No error handling for when user creates an account with an existing name
- [ ] No handling for successful account creation
- [ ] No error handling in "add user" at menu bar when the user does not exist.
- [ ] The user can send message without selecting a user or when the contact list is empty. (which is wrong)
- [ ] add user menu bar: the same user can be added multiple times to the contact list, which should be unique.
- [ ] add user menu bar: the server gives an error (probably) and the interface doesn't do nothing, only adds the same contact to the list again as above
- [ ] all text fields don't clear to empty string after the user clicked submited
- [ ] the server has to set new timeout if data is not complete or else the server will never read any data since the protocol only reads that when the timeout isn't expired.
- [ ] the receiver user of the sender object makes the add user message to be added as "sender" instead of receiver in the ui.
- [ ] JPanel (where it shows messages) *is* scrollable however it doesn't implement the correct behavior for a chat application (which is notable).
- [ ] the user can't send an empty message.
- [ ] interface is not responsive (it is a bit because of borderbox, however is not perfect)
- [ ] login should not accept empty passwords
- [ ] login is missing error label and the proper handling
- [ ] fix possible socket connection leaks
- [ ] database design flaw: can't delete user contacts.
- [ ] no cryptography to messages (2-way)
- [ ] no cryptography to passwords (hash)
- [ ] design flaw: The frontend should not be in-memory (but it is for easy of implementation, currently)

package maildrop.service;

import maildrop.entity.Maildrop;
import maildrop.entity.Message;
import user.entity.User;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;

public class MaildropManager {
    private final MessageManager messageManager;

    private MaildropManager(MessageManager messageManager) {
        this.messageManager = messageManager;
    }
    
    public Maildrop getUserMaildrop(User user) throws SQLException {
        Maildrop maildrop = new Maildrop();
        List<Message> messages = messageManager.findAllMessagesByToEmail(user.getEmail());

        int octetSize;
        int totalOctetSize = 0;
        int messageNumber = 1;
        for (Message message : messages) {
            octetSize = message.toString().getBytes(Charset.forName(message.getCharset())).length;
            message.setOctetCount(octetSize);
            message.setMessageNumber(messageNumber);

            totalOctetSize += octetSize;
            messageNumber++;
        }

        maildrop.setMessages(messages);
        maildrop.setNotMarkedMessageOctetCount(totalOctetSize);
        maildrop.setNotMarkedMessageCount(messages.size());

        return maildrop;
    }

    public static class Builder {
        private MessageManager messageManager;

        public Builder setMessageManager(MessageManager messageManager) {
            this.messageManager = messageManager;

            return this;
        }

        public MaildropManager build() {
            return new MaildropManager(messageManager);
        }
    }
}

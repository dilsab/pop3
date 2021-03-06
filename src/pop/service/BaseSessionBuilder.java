package pop.service;

import maildrop.repository.MessageRepository;
import maildrop.service.MaildropManager;
import maildrop.service.MessageManager;
import pop.service.popcommand.*;
import security.service.Encryptor;
import user.repository.UserRepository;
import user.service.UserManager;

public class BaseSessionBuilder {
    public static PopSessionThread.Builder getPopSessionThreadBuilder() {
        PopSessionThread.Builder popSessionThreadBuilder = new PopSessionThread.Builder();

        popSessionThreadBuilder
                .setPopCommandExecutor(new PopCommandExecutor.Builder()
                        .addPopCommand(new RegisterCommandExecutor.Builder()
                                .setEncryptor(new Encryptor.Builder()
                                        .setIterations(10000)
                                        .setPbeKeyLength(256)
                                        .setSaltLength(32)
                                        .setDelimiter(":")
                                        .build()
                                )
                                .setUserRepository(new UserRepository.Builder()
                                        .build())
                                .setUserManager(new UserManager.Builder()
                                        .build()
                                )
                                .build(),
                                "REGISTER"
                        )
                        .addPopCommand(new UserCommandExecutor.Builder()
                                .setUserRepository(new UserRepository.Builder()
                                        .build()
                                )
                                .build(),
                                "USER"
                        )
                        .addPopCommand(new PassCommandExecutor.Builder()
                                .setEncryptor(new Encryptor.Builder()
                                        .setIterations(10000)
                                        .setPbeKeyLength(256)
                                        .setSaltLength(32)
                                        .setDelimiter(":")
                                        .build()
                                )
                                .setMaildropManager(new MaildropManager.Builder()
                                        .setMessageManager(new MessageManager.Builder()
                                                .setMessageRepository(new MessageRepository.Builder()
                                                        .build()
                                                )
                                                .build()
                                        )
                                        .build()
                                )
                                .build(),
                                "PASS"
                        )
                        .addPopCommand(new StatCommandExecutor.Builder()
                                .build(),
                                "STAT"
                        )
                        .addPopCommand(new ListCommandExecutor.Builder()
                                .build(),
                                "LIST"
                        )
                        .addPopCommand(new RetrCommandExecutor.Builder()
                                .build(),
                                "RETR"
                        )
                        .addPopCommand(new NoopCommandExecutor.Builder()
                                .build(),
                                "NOOP"
                        )
                        .addPopCommand(new DeleCommandExecutor.Builder()
                                .build(),
                                "DELE"
                        )
                        .addPopCommand(new RsetCommandExecutor.Builder()
                                .build(),
                                "RSET"
                        )
                        .addPopCommand(new QuitCommandExecutor.Builder()
                                .setMessageManager(new MessageManager.Builder()
                                        .build()
                                )
                                .build(),
                                "QUIT"
                        )
                        .build()
                )
        ;

        return popSessionThreadBuilder;
    }
}

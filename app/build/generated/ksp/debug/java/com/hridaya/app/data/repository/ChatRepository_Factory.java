package com.hridaya.app.data.repository;

import com.google.ai.client.generativeai.GenerativeModel;
import com.hridaya.app.data.db.MessageDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class ChatRepository_Factory implements Factory<ChatRepository> {
  private final Provider<MessageDao> messageDaoProvider;

  private final Provider<GenerativeModel> generativeModelProvider;

  public ChatRepository_Factory(Provider<MessageDao> messageDaoProvider,
      Provider<GenerativeModel> generativeModelProvider) {
    this.messageDaoProvider = messageDaoProvider;
    this.generativeModelProvider = generativeModelProvider;
  }

  @Override
  public ChatRepository get() {
    return newInstance(messageDaoProvider.get(), generativeModelProvider.get());
  }

  public static ChatRepository_Factory create(Provider<MessageDao> messageDaoProvider,
      Provider<GenerativeModel> generativeModelProvider) {
    return new ChatRepository_Factory(messageDaoProvider, generativeModelProvider);
  }

  public static ChatRepository newInstance(MessageDao messageDao, GenerativeModel generativeModel) {
    return new ChatRepository(messageDao, generativeModel);
  }
}

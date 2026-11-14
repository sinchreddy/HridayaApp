package com.hridaya.app.viewmodel;

import com.hridaya.app.data.repository.ChatRepository;
import com.hridaya.app.data.repository.MoodRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class ChatViewModel_Factory implements Factory<ChatViewModel> {
  private final Provider<ChatRepository> chatRepositoryProvider;

  private final Provider<MoodRepository> moodRepositoryProvider;

  public ChatViewModel_Factory(Provider<ChatRepository> chatRepositoryProvider,
      Provider<MoodRepository> moodRepositoryProvider) {
    this.chatRepositoryProvider = chatRepositoryProvider;
    this.moodRepositoryProvider = moodRepositoryProvider;
  }

  @Override
  public ChatViewModel get() {
    return newInstance(chatRepositoryProvider.get(), moodRepositoryProvider.get());
  }

  public static ChatViewModel_Factory create(Provider<ChatRepository> chatRepositoryProvider,
      Provider<MoodRepository> moodRepositoryProvider) {
    return new ChatViewModel_Factory(chatRepositoryProvider, moodRepositoryProvider);
  }

  public static ChatViewModel newInstance(ChatRepository chatRepository,
      MoodRepository moodRepository) {
    return new ChatViewModel(chatRepository, moodRepository);
  }
}

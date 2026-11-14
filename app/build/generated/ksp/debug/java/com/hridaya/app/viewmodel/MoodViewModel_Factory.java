package com.hridaya.app.viewmodel;

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
public final class MoodViewModel_Factory implements Factory<MoodViewModel> {
  private final Provider<MoodRepository> moodRepositoryProvider;

  public MoodViewModel_Factory(Provider<MoodRepository> moodRepositoryProvider) {
    this.moodRepositoryProvider = moodRepositoryProvider;
  }

  @Override
  public MoodViewModel get() {
    return newInstance(moodRepositoryProvider.get());
  }

  public static MoodViewModel_Factory create(Provider<MoodRepository> moodRepositoryProvider) {
    return new MoodViewModel_Factory(moodRepositoryProvider);
  }

  public static MoodViewModel newInstance(MoodRepository moodRepository) {
    return new MoodViewModel(moodRepository);
  }
}

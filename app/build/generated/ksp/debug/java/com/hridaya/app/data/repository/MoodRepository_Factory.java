package com.hridaya.app.data.repository;

import com.hridaya.app.data.db.MoodDao;
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
public final class MoodRepository_Factory implements Factory<MoodRepository> {
  private final Provider<MoodDao> moodDaoProvider;

  public MoodRepository_Factory(Provider<MoodDao> moodDaoProvider) {
    this.moodDaoProvider = moodDaoProvider;
  }

  @Override
  public MoodRepository get() {
    return newInstance(moodDaoProvider.get());
  }

  public static MoodRepository_Factory create(Provider<MoodDao> moodDaoProvider) {
    return new MoodRepository_Factory(moodDaoProvider);
  }

  public static MoodRepository newInstance(MoodDao moodDao) {
    return new MoodRepository(moodDao);
  }
}

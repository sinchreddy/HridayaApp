package com.hridaya.app.di;

import com.hridaya.app.data.db.HridayaDatabase;
import com.hridaya.app.data.db.MoodDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class AppModule_ProvideMoodDaoFactory implements Factory<MoodDao> {
  private final Provider<HridayaDatabase> dbProvider;

  public AppModule_ProvideMoodDaoFactory(Provider<HridayaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public MoodDao get() {
    return provideMoodDao(dbProvider.get());
  }

  public static AppModule_ProvideMoodDaoFactory create(Provider<HridayaDatabase> dbProvider) {
    return new AppModule_ProvideMoodDaoFactory(dbProvider);
  }

  public static MoodDao provideMoodDao(HridayaDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideMoodDao(db));
  }
}

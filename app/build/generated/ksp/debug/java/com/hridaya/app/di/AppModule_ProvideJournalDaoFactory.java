package com.hridaya.app.di;

import com.hridaya.app.data.db.HridayaDatabase;
import com.hridaya.app.data.db.JournalDao;
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
public final class AppModule_ProvideJournalDaoFactory implements Factory<JournalDao> {
  private final Provider<HridayaDatabase> dbProvider;

  public AppModule_ProvideJournalDaoFactory(Provider<HridayaDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public JournalDao get() {
    return provideJournalDao(dbProvider.get());
  }

  public static AppModule_ProvideJournalDaoFactory create(Provider<HridayaDatabase> dbProvider) {
    return new AppModule_ProvideJournalDaoFactory(dbProvider);
  }

  public static JournalDao provideJournalDao(HridayaDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideJournalDao(db));
  }
}

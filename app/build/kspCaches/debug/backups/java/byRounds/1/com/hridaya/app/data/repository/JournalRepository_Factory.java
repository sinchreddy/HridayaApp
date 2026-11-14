package com.hridaya.app.data.repository;

import com.hridaya.app.data.db.JournalDao;
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
public final class JournalRepository_Factory implements Factory<JournalRepository> {
  private final Provider<JournalDao> journalDaoProvider;

  public JournalRepository_Factory(Provider<JournalDao> journalDaoProvider) {
    this.journalDaoProvider = journalDaoProvider;
  }

  @Override
  public JournalRepository get() {
    return newInstance(journalDaoProvider.get());
  }

  public static JournalRepository_Factory create(Provider<JournalDao> journalDaoProvider) {
    return new JournalRepository_Factory(journalDaoProvider);
  }

  public static JournalRepository newInstance(JournalDao journalDao) {
    return new JournalRepository(journalDao);
  }
}

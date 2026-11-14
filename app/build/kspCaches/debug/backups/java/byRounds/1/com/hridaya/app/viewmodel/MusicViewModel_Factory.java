package com.hridaya.app.viewmodel;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class MusicViewModel_Factory implements Factory<MusicViewModel> {
  private final Provider<Context> contextProvider;

  public MusicViewModel_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public MusicViewModel get() {
    return newInstance(contextProvider.get());
  }

  public static MusicViewModel_Factory create(Provider<Context> contextProvider) {
    return new MusicViewModel_Factory(contextProvider);
  }

  public static MusicViewModel newInstance(Context context) {
    return new MusicViewModel(context);
  }
}

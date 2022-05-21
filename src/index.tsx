import { NativeModules, Platform, requireNativeComponent } from 'react-native';

const LINKING_ERROR =
  `The package 'rn-music-files' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

const MusicFiles = NativeModules.MusicFiles
  ? NativeModules.MusicFiles
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
export default MusicFiles;

export const Constants = {
  SortBy: {
    Artist: 'ARTIST',
    Album: 'ALBUM',
    Title: 'TITLE',
  },
  SortOrder: {
    Ascending: 'ASC',
    Descending: 'DESC',
  },
};

export const CoverImage = requireNativeComponent('RCTCoverImageView');

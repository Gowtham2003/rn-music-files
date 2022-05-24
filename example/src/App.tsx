import React, { useState } from 'react';
import {
  Button,
  Image,
  PermissionsAndroid,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from 'react-native';
import MusicFiles, { Constants, CoverImage } from 'rn-music-files';

export default function App() {
  const [state, setState] = useState({
    searchParam: '',
    getSongsSearchParamArtist: null,
    getSongsSearchParamAlbum: null,
    search: [],
  });
  const requestPermission = async () => {
    try {
      const granted = await PermissionsAndroid.requestMultiple([
        PermissionsAndroid.PERMISSIONS.READ_EXTERNAL_STORAGE,
        PermissionsAndroid.PERMISSIONS.WRITE_EXTERNAL_STORAGE,
      ]);
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        alert('You can use the package');
      } else {
        requestPermission();
      }
    } catch (err) {
      console.warn(err);
    }
  };
  const search = (searchParam) => {
    MusicFiles.search({
      searchParam,
      batchSize: 0,
      batchNumber: 0,
      sortBy: Constants.SortBy.Title,
      sortOrder: Constants.SortOrder.Ascending,
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er.message)));
  };
  const getAll = () => {
    MusicFiles.getAll({
      cover: true,
      batchSize: 0,
      batchNumber: 0,
      sortBy: Constants.SortBy.Title,
      sortOrder: Constants.SortOrder.Ascending,
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er)));
  };
  const getArtists = () => {
    MusicFiles.getArtists({
      batchSize: 0,
      batchNumber: 0,
      sortBy: Constants.SortBy.Artist,
      sortOrder: Constants.SortOrder.Ascending,
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er)));
  };
  const getAlbums = (searchParam) => {
    MusicFiles.getAlbums({
      artist: searchParam,
      batchSize: 0,
      batchNumber: 0,
      sortBy: Constants.SortBy.Artist,
      sortOrder: Constants.SortOrder.Ascending,
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er)));
  };
  const getSongs = (album, artist) => {
    MusicFiles.getSongs({
      artist: artist,
      album: album,
      batchSize: 0,
      batchNumber: 0,
      sortBy: Constants.SortBy.Artist,
      sortOrder: Constants.SortOrder.Ascending,
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er)));
  };
  const getSongsByPath = () => {
    MusicFiles.getSongsByPath({
      cover: true,
      coverFolder: '/storage/emulated/0/Download/Covers',
      path: '/storage/emulated/0/Music/',
    })
      .then((f) => {
        setState({ ...state, search: f });
      })
      .catch((er) => console.log(JSON.stringify(er.message)));
  };
  return (
    <View style={styles.container}>
      <ScrollView style={styles.scrollVIew}>
        <Text />
        <CoverImage
          source={'/storage/emulated/0/Download/Hurt - Johnny Cash.mp3'}
          placeHolder={
            'https://cdn2.iconfinder.com/data/icons/Qetto___icons_by_ampeross-d4njobq/256/library-music.png'
          }
          width={120}
          height={120}
        />
        <Text />
        <TextInput
          placeholder="search param"
          onChangeText={(v) => setState({ ...state, searchParam: v })}
          style={styles.input}
        />

        <Text />
        <TextInput
          placeholder="getSongs search param: Artist"
          onChangeText={(v) =>
            setState({ ...state, getSongsSearchParamArtist: v })
          }
          style={styles.input}
        />
        <Text />
        <TextInput
          placeholder="getSongs search param: Album"
          onChangeText={(v) =>
            setState({ ...state, getSongsSearchParamAlbum: v })
          }
          style={styles.input}
        />
        <Text />
        <Button title="search" onPress={() => search(state.searchParam)} />

        <Text />
        <Button title="getall" onPress={() => getAll()} />

        <Text />
        <Button title="getArtists" onPress={() => getArtists()} />

        <Text />
        <Button
          title="getAlbums"
          onPress={() => getAlbums(state.searchParam)}
        />
        <Text />
        <Button title="getSongsByPath" onPress={() => getSongsByPath()} />

        <Text />
        <Button
          title="getSongs"
          onPress={() =>
            getSongs(
              state.getSongsSearchParamAlbum,
              state.getSongsSearchParamArtist
            )
          }
        />

        <Text style={styles.instructions}>
          results : {JSON.stringify(state.search)}
        </Text>
        {state?.search?.map((item) => (
          <Image
            style={{ backgroundColor: 'red', width: 50, height: 50 }}
            key={item.id}
            source={{ uri: 'file://' + item?.cover }}
          />
        ))}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    alignContent: 'center',
    backgroundColor: '#F5FCFF',
  },
  scrollVIew: {
    width: '80%',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
    alignSelf: 'center',
    justifyContent: 'center',
  },
  input: {
    borderColor: 'gray',
    borderWidth: 2,
    width: '100%',
    height: 40,
    textAlign: 'center',
    alignSelf: 'center',
    justifyContent: 'center',
  },
});

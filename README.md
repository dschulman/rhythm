# Rhythm #

Rhythm is a nonverbal behavior annotator for virtual characters: given
text, it will generate appropriate nonverbal behavior for that text,
including eye movement, head movement, hand gestures, and postural changes.
The resulting annotated text can be used as a basis for hand-edited 
animation scripts, or could be directly given to character animation.
Rhythm can be seen as a clean reimplementation of earlier academic
projects, primarily [BEAT](www.ru.is/faculty/hannes/beat/).

It is currently in an embryonic state, so do not expect it to do much
at the moment.

## Model Files ##

Rhythm relies on a shallow linguistic analysis of the text, performed
using a combination of its own heuristics and some open-source 
libraries: [OpenNLP](http://incubator.apache.org/opennlp/) for statistical
natural language processing, and [Wordnet](http://wordnet.princeton.edu)
(via [JWI](http://projects.csail.mit.edu/jwi/)) to look up word relations.

Most of this processing requires various model files: dictionaries and
trained statistical models.  For space reasons, most of these models 
are not included with Rhythm, and need to be downloaded.

### OpenNLP model files ###

OpenNLP model files can be downloaded from <http://opennlp.sourceforge.net/models-1.5/>.  
Rhythm will need models for 
[sentence detection](http://opennlp.sourceforge.net/models-1.5/en-sent.bin),
[tokenization](http://opennlp.sourceforge.net/models-1.5/en-token.bin),
[part of speech tagging](http://opennlp.sourceforge.net/models-1.5/en-pos-maxent.bin),
and [phrase chunking](http://opennlp.sourceforge.net/models-1.5/en-chunker.bin).

### Wordnet dictionary ###

Only the Wordnet [database files](http://wordnetcode.princeton.edu/3.0/WNdb-3.0.tar.gz)
are required, although it will work fine if you install a full distribution; rhythm
just needs to be told where to look for them.

### Other model files ###

A [dictionary](en-discourse-markers) used to identify discourse markers 
(e.g., for topic shifts) is included with Rhythm.  You shouldn't need
to worry about it unless you want to tweak this file, or want to work in
a language other than English.
